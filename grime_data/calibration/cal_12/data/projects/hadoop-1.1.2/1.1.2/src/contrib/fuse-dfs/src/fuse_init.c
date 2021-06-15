/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
#include <strings.h>

#include "fuse_dfs.h"
#include "fuse_init.h"
#include "fuse_options.h"
#include "fuse_context_handle.h"

// Hacked up function to basically do:
//  protectedpaths = split(options.protected,':');

void init_protectedpaths(dfs_context *dfs) {

  char *tmp = options.protected;


  // handle degenerate case up front.
  if (tmp == NULL || 0 == *tmp) {
    dfs->protectedpaths = (char**)malloc(sizeof(char*));
    dfs->protectedpaths[0] = NULL;
    return;
  }
  assert(tmp);

  if (options.debug) {
    print_options();
  }


  int i = 0;
  while (tmp && (NULL != (tmp = index(tmp,':')))) {
    tmp++; // pass the ,
    i++;
  }
  i++; // for the last entry
  i++; // for the final NULL
  dfs->protectedpaths = (char**)malloc(sizeof(char*)*i);
  assert(dfs->protectedpaths);
  tmp = options.protected;
  int j  = 0;
  while (NULL != tmp && j < i) {
    int length;
    char *eos = index(tmp,':');
    if (NULL != eos) {
      length = eos - tmp; // length of this value
    } else {
      length = strlen(tmp);
    }
    dfs->protectedpaths[j] = (char*)malloc(sizeof(char)*length+1);
    assert(dfs->protectedpaths[j]);
    strncpy(dfs->protectedpaths[j], tmp, length);
    dfs->protectedpaths[j][length] = '\0';
    if (eos) {
      tmp = eos + 1;
    } else {
      tmp = NULL;
    }
    j++;
  }
  dfs->protectedpaths[j] = NULL;

  /*
    j  = 0;
    while (dfs->protectedpaths[j]) {
    printf("dfs->protectedpaths[%d]=%s\n",j,dfs->protectedpaths[j]);
    fflush(stdout);
    j++;
    }
    exit(1);
  */
}

void *dfs_init()
{

  //
  // Create a private struct of data we will pass to fuse here and which
  // will then be accessible on every call.
  //
  dfs_context *dfs = (dfs_context*)malloc(sizeof (dfs_context));

  if (NULL == dfs) {
    syslog(LOG_ERR, "FATAL: could not malloc fuse dfs context struct - out of memory %s:%d", __FILE__, __LINE__);
    exit(1);
  }

  // initialize the context
  dfs->debug                 = options.debug;
  dfs->nn_hostname           = options.server;
  dfs->nn_port               = options.port;
  dfs->fs                    = NULL;
  dfs->read_only             = options.read_only;
  dfs->usetrash              = options.usetrash;
  dfs->protectedpaths        = NULL;
  dfs->rdbuffer_size         = options.rdbuffer_size;
  dfs->direct_io             = options.direct_io;

  bzero(dfs->dfs_uri,0);
  sprintf(dfs->dfs_uri,"dfs://%s:%d/",dfs->nn_hostname,dfs->nn_port);
  dfs->dfs_uri_len = strlen(dfs->dfs_uri);

  // use ERR level to ensure it makes it into the log.
  syslog(LOG_ERR, "mounting %s", dfs->dfs_uri);

  init_protectedpaths(dfs);
  assert(dfs->protectedpaths != NULL);

  if (dfs->rdbuffer_size <= 0) {
    syslog(LOG_DEBUG, "WARN: dfs->rdbuffersize <= 0 = %ld %s:%d", dfs->rdbuffer_size, __FILE__, __LINE__);
    dfs->rdbuffer_size = 32768;
  }
  return (void*)dfs;
}



void dfs_destroy (void *ptr)
{
  TRACE("destroy")
  dfs_context *dfs = (dfs_context*)ptr;
  dfs->fs = NULL;
}
