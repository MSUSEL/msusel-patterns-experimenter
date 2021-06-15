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
#include "fuse_dfs.h"
#include "fuse_options.h"
#include "fuse_impls.h"
#include "fuse_init.h"


int is_protected(const char *path) {

  dfs_context *dfs = (dfs_context*)fuse_get_context()->private_data;
  assert(dfs != NULL);
  assert(dfs->protectedpaths);

  int i ;
  for (i = 0; dfs->protectedpaths[i]; i++) {
    if (strcmp(path, dfs->protectedpaths[i]) == 0) {
      return 1;
    }
  }
  return 0;
}

static struct fuse_operations dfs_oper = {
  .getattr	= dfs_getattr,
  .access	= dfs_access,
  .readdir	= dfs_readdir,
  .destroy       = dfs_destroy,
  .init         = dfs_init,
  .open	        = dfs_open,
  .read	        = dfs_read,
  .symlink	= dfs_symlink,
  .statfs	= dfs_statfs,
  .mkdir	= dfs_mkdir,
  .rmdir	= dfs_rmdir,
  .rename	= dfs_rename,
  .unlink       = dfs_unlink,
  .release      = dfs_release,
  .create       = dfs_create,
  .write	= dfs_write,
  .flush        = dfs_flush,
  .mknod        = dfs_mknod,
	.utimens	= dfs_utimens,
  .chmod	= dfs_chmod,
  .chown	= dfs_chown,
  .truncate	= dfs_truncate,
};


int main(int argc, char *argv[])
{

  umask(0);

  extern const char *program;  
  program = argv[0];
  struct fuse_args args = FUSE_ARGS_INIT(argc, argv);

  /* clear structure that holds our options */
  memset(&options, 0, sizeof(struct options));

  // some defaults
  options.rdbuffer_size = 10*1024*1024; 
  options.attribute_timeout = 60; 
  options.entry_timeout = 60;

  if (fuse_opt_parse(&args, &options, dfs_opts, dfs_options) == -1)
    /** error parsing options */
    return -1;


  // Some fuse options we set
  if (! options.private) {
    fuse_opt_add_arg(&args, "-oallow_other");
  }

  if (!options.no_permissions) {
    fuse_opt_add_arg(&args, "-odefault_permissions");
  }

  {
    char buf[1024];

    snprintf(buf, sizeof buf, "-oattr_timeout=%d",options.attribute_timeout);
    fuse_opt_add_arg(&args, buf);

    snprintf(buf, sizeof buf, "-oentry_timeout=%d",options.entry_timeout);
    fuse_opt_add_arg(&args, buf);
  }

  if (options.server == NULL || options.port == 0) {
    print_usage(argv[0]);
    exit(0);
  }


  // 
  // Check we can connect to hdfs
  // 
  if (options.initchecks == 1) {
    hdfsFS temp;
    if ((temp = hdfsConnect(options.server, options.port)) == NULL) {
      const char *cp = getenv("CLASSPATH");
      const char *ld = getenv("LD_LIBRARY_PATH");
      fprintf(stderr, "FATAL: misconfiguration problem, cannot connect to hdfs - here's your environment\n");
      fprintf(stderr, "LD_LIBRARY_PATH=%s\n",ld == NULL ? "NULL" : ld);
      fprintf(stderr, "CLASSPATH=%s\n",cp == NULL ? "NULL" : cp);
      syslog(LOG_ERR, "FATAL: misconfiguration problem, cannot connect to hdfs - here's your environment\n");
      syslog(LOG_ERR, "LD_LIBRARY_PATH=%s\n",ld == NULL ? "NULL" : ld);
      syslog(LOG_ERR, "CLASSPATH=%s\n",cp == NULL ? "NULL" : cp);
      exit(0);
    }  
    temp = NULL;
  }

  int ret = fuse_main(args.argc, args.argv, &dfs_oper, NULL);

  if (ret) printf("\n");

  /** free arguments */
  fuse_opt_free_args(&args);

  return ret;
}
