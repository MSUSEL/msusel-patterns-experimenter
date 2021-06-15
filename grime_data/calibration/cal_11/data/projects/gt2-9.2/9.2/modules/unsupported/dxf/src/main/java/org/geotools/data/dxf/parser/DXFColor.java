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
package org.geotools.data.dxf.parser;

import java.awt.Color;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 *
 * @source $URL$
 */
public final class DXFColor {

    private static final Log log = LogFactory.getLog(DXFColor.class);
    private static int defaultColor = 0;
    static final Color[] ColorMap = {
        new Color(0xff, 0xff, 0xff), // color 255 white
        new Color(0xff, 0x00, 0x00), // color 1	Red
        new Color(0xff, 0xff, 0x00), // color 2	Yellow
        new Color(0x00, 0xff, 0x00), // color 3	Green
        new Color(0x00, 0xff, 0xff), // color 4 Cyan
        new Color(0x00, 0x00, 0xff), // color 5	Blue
        new Color(0xff, 0x00, 0xff), // color 6 Magenta
        new Color(0xff, 0xff, 0xff), // color 7	White
        new Color(0x98, 0x98, 0x98), // color 8	Grey
        new Color(0xc0, 0xc0, 0xc0), // color 9
        new Color(0xff, 0x00, 0x00), // color 10
        new Color(0xff, 0x80, 0x80), // color 11
        new Color(0xa6, 0x00, 0x00), // color 12
        new Color(0xa6, 0x53, 0x53), // color 13
        new Color(0x80, 0x00, 0x00), // color 14
        new Color(0x80, 0x40, 0x40), // color 15
        new Color(0x4c, 0x00, 0x00), // color 16
        new Color(0x4c, 0x26, 0x26), // color 17
        new Color(0x26, 0x00, 0x00), // color 18
        new Color(0x26, 0x13, 0x13), // color 19
        new Color(0xff, 0x40, 0x00), // color 20
        new Color(0xff, 0x9f, 0x80), // color 21
        new Color(0xa6, 0x29, 0x00), // color 22
        new Color(0xa6, 0x68, 0x53), // color 23
        new Color(0x80, 0x20, 0x00), // color 24
        new Color(0x80, 0x50, 0x40), // color 25
        new Color(0x4c, 0x13, 0x00), // color 26
        new Color(0x4c, 0x30, 0x26), // color 27
        new Color(0x26, 0x0a, 0x00), // color 28
        new Color(0x26, 0x18, 0x13), // color 29
        new Color(0xff, 0x80, 0x00), // color 30
        new Color(0xff, 0xbf, 0x80), // color 31
        new Color(0xa6, 0x53, 0x00), // color 32
        new Color(0xa6, 0x7c, 0x53), // color 33
        new Color(0x80, 0x40, 0x00), // color 34
        new Color(0x80, 0x60, 0x40), // color 35
        new Color(0x4c, 0x26, 0x00), // color 36
        new Color(0x4c, 0x39, 0x26), // color 37
        new Color(0x26, 0x13, 0x00), // color 38
        new Color(0x26, 0x1d, 0x13), // color 39
        new Color(0xff, 0xbf, 0x00), // color 40
        new Color(0xff, 0xdf, 0x80), // color 41
        new Color(0xa6, 0x7c, 0x00), // color 42
        new Color(0xa6, 0x91, 0x53), // color 43
        new Color(0x80, 0x60, 0x00), // color 44
        new Color(0x80, 0x70, 0x40), // color 45
        new Color(0x4c, 0x39, 0x00), // color 46
        new Color(0x4c, 0x43, 0x26), // color 47
        new Color(0x26, 0x1d, 0x00), // color 48
        new Color(0x26, 0x21, 0x13), // color 49
        new Color(0xff, 0xff, 0x00), // color 50
        new Color(0xff, 0xff, 0x80), // color 51
        new Color(0xa6, 0xa6, 0x00), // color 52
        new Color(0xa6, 0xa6, 0x53), // color 53
        new Color(0x80, 0x80, 0x00), // color 54
        new Color(0x80, 0x80, 0x40), // color 55
        new Color(0x4c, 0x4c, 0x00), // color 56
        new Color(0x4c, 0x4c, 0x26), // color 57
        new Color(0x26, 0x26, 0x00), // color 58
        new Color(0x26, 0x26, 0x13), // color 59
        new Color(0xbf, 0xff, 0x00), // color 60
        new Color(0xdf, 0xff, 0x80), // color 61
        new Color(0x7c, 0xa6, 0x00), // color 62
        new Color(0x91, 0xa6, 0x53), // color 63
        new Color(0x60, 0x80, 0x00), // color 64
        new Color(0x70, 0x80, 0x40), // color 65
        new Color(0x39, 0x4c, 0x00), // color 66
        new Color(0x43, 0x4c, 0x26), // color 67
        new Color(0x1d, 0x26, 0x00), // color 68
        new Color(0x21, 0x26, 0x13), // color 69
        new Color(0x80, 0xff, 0x00), // color 70
        new Color(0xbf, 0xff, 0x80), // color 71
        new Color(0x53, 0xa6, 0x00), // color 72
        new Color(0x7c, 0xa6, 0x53), // color 73
        new Color(0x40, 0x80, 0x00), // color 74
        new Color(0x60, 0x80, 0x40), // color 75
        new Color(0x26, 0x4c, 0x00), // color 76
        new Color(0x39, 0x4c, 0x26), // color 77
        new Color(0x13, 0x26, 0x00), // color 78
        new Color(0x1d, 0x26, 0x13), // color 79
        new Color(0x40, 0xff, 0x00), // color 80
        new Color(0x9f, 0xff, 0x80), // color 81
        new Color(0x29, 0xa6, 0x00), // color 82
        new Color(0x68, 0xa6, 0x53), // color 83
        new Color(0x20, 0x80, 0x00), // color 84
        new Color(0x50, 0x80, 0x40), // color 85
        new Color(0x13, 0x4c, 0x00), // color 86
        new Color(0x30, 0x4c, 0x26), // color 87
        new Color(0x0a, 0x26, 0x00), // color 88
        new Color(0x18, 0x26, 0x13), // color 89
        new Color(0x00, 0xff, 0x00), // color 90
        new Color(0x80, 0xff, 0x80), // color 91
        new Color(0x00, 0xa6, 0x00), // color 92
        new Color(0x53, 0xa6, 0x53), // color 93
        new Color(0x00, 0x80, 0x00), // color 94
        new Color(0x40, 0x80, 0x40), // color 95
        new Color(0x00, 0x4c, 0x00), // color 96
        new Color(0x26, 0x4c, 0x26), // color 97
        new Color(0x00, 0x26, 0x00), // color 98
        new Color(0x13, 0x26, 0x13), // color 99
        new Color(0x00, 0xff, 0x40), // color 100
        new Color(0x80, 0xff, 0x9f), // color 101
        new Color(0x00, 0xa6, 0x29), // color 102
        new Color(0x53, 0xa6, 0x68), // color 103
        new Color(0x00, 0x80, 0x20), // color 104
        new Color(0x40, 0x80, 0x50), // color 105
        new Color(0x00, 0x4c, 0x13), // color 106
        new Color(0x26, 0x4c, 0x30), // color 107
        new Color(0x00, 0x26, 0x0a), // color 108
        new Color(0x13, 0x26, 0x18), // color 109
        new Color(0x00, 0xff, 0x80), // color 110
        new Color(0x80, 0xff, 0xbf), // color 111
        new Color(0x00, 0xa6, 0x53), // color 112
        new Color(0x53, 0xa6, 0x7c), // color 113
        new Color(0x00, 0x80, 0x40), // color 114
        new Color(0x40, 0x80, 0x60), // color 115
        new Color(0x00, 0x4c, 0x26), // color 116
        new Color(0x26, 0x4c, 0x39), // color 117
        new Color(0x00, 0x26, 0x13), // color 118
        new Color(0x13, 0x26, 0x1d), // color 119
        new Color(0x00, 0xff, 0xbf), // color 120
        new Color(0x80, 0xff, 0xdf), // color 121
        new Color(0x00, 0xa6, 0x7c), // color 122
        new Color(0x53, 0xa6, 0x91), // color 123
        new Color(0x00, 0x80, 0x60), // color 124
        new Color(0x40, 0x80, 0x70), // color 125
        new Color(0x00, 0x4c, 0x39), // color 126
        new Color(0x26, 0x4c, 0x43), // color 127
        new Color(0x00, 0x26, 0x1d), // color 128
        new Color(0x13, 0x26, 0x21), // color 129
        new Color(0x00, 0xff, 0xff), // color 130
        new Color(0x80, 0xff, 0xff), // color 131
        new Color(0x00, 0xa6, 0xa6), // color 132
        new Color(0x53, 0xa6, 0xa6), // color 133
        new Color(0x00, 0x80, 0x80), // color 134
        new Color(0x40, 0x80, 0x80), // color 135
        new Color(0x00, 0x4c, 0x4c), // color 136
        new Color(0x26, 0x4c, 0x4c), // color 137
        new Color(0x00, 0x26, 0x26), // color 138
        new Color(0x13, 0x26, 0x26), // color 139
        new Color(0x00, 0xbf, 0xff), // color 140
        new Color(0x80, 0xdf, 0xff), // color 141
        new Color(0x00, 0x7c, 0xa6), // color 142
        new Color(0x53, 0x91, 0xa6), // color 143
        new Color(0x00, 0x60, 0x80), // color 144
        new Color(0x40, 0x70, 0x80), // color 145
        new Color(0x00, 0x39, 0x4c), // color 146
        new Color(0x26, 0x43, 0x4c), // color 147
        new Color(0x00, 0x1d, 0x26), // color 148
        new Color(0x13, 0x21, 0x26), // color 149
        new Color(0x00, 0x80, 0xff), // color 150
        new Color(0x80, 0xbf, 0xff), // color 151
        new Color(0x00, 0x53, 0xa6), // color 152
        new Color(0x53, 0x7c, 0xa6), // color 153
        new Color(0x00, 0x40, 0x80), // color 154
        new Color(0x40, 0x60, 0x80), // color 155
        new Color(0x00, 0x26, 0x4c), // color 156
        new Color(0x26, 0x39, 0x4c), // color 157
        new Color(0x00, 0x13, 0x26), // color 158
        new Color(0x13, 0x1d, 0x26), // color 159
        new Color(0x00, 0x40, 0xff), // color 160
        new Color(0x80, 0x9f, 0xff), // color 161
        new Color(0x00, 0x29, 0xa6), // color 162
        new Color(0x53, 0x68, 0xa6), // color 163
        new Color(0x00, 0x20, 0x80), // color 164
        new Color(0x40, 0x50, 0x80), // color 165
        new Color(0x00, 0x13, 0x4c), // color 166
        new Color(0x26, 0x30, 0x4c), // color 167
        new Color(0x00, 0x0a, 0x26), // color 168
        new Color(0x13, 0x18, 0x26), // color 169
        new Color(0x00, 0x00, 0xff), // color 170
        new Color(0x80, 0x80, 0xff), // color 171
        new Color(0x00, 0x00, 0xa6), // color 172
        new Color(0x53, 0x53, 0xa6), // color 173
        new Color(0x00, 0x00, 0x80), // color 174
        new Color(0x40, 0x40, 0x80), // color 175
        new Color(0x00, 0x00, 0x4c), // color 176
        new Color(0x26, 0x26, 0x4c), // color 177
        new Color(0x00, 0x00, 0x26), // color 178
        new Color(0x13, 0x13, 0x26), // color 179
        new Color(0x40, 0x00, 0xff), // color 180
        new Color(0x9f, 0x80, 0xff), // color 181
        new Color(0x29, 0x00, 0xa6), // color 182
        new Color(0x68, 0x53, 0xa6), // color 183
        new Color(0x20, 0x00, 0x80), // color 184
        new Color(0x50, 0x40, 0x80), // color 185
        new Color(0x13, 0x00, 0x4c), // color 186
        new Color(0x30, 0x26, 0x4c), // color 187
        new Color(0x0a, 0x00, 0x26), // color 188
        new Color(0x18, 0x13, 0x26), // color 189
        new Color(0x80, 0x00, 0xff), // color 190
        new Color(0xbf, 0x80, 0xff), // color 191
        new Color(0x53, 0x00, 0xa6), // color 192
        new Color(0x7c, 0x53, 0xa6), // color 193
        new Color(0x40, 0x00, 0x80), // color 194
        new Color(0x60, 0x40, 0x80), // color 195
        new Color(0x26, 0x00, 0x4c), // color 196
        new Color(0x39, 0x26, 0x4c), // color 197
        new Color(0x13, 0x00, 0x26), // color 198
        new Color(0x1d, 0x13, 0x26), // color 199
        new Color(0xbf, 0x00, 0xff), // color 200
        new Color(0xdf, 0x80, 0xff), // color 201
        new Color(0x7c, 0x00, 0xa6), // color 202
        new Color(0x91, 0x53, 0xa6), // color 203
        new Color(0x60, 0x00, 0x80), // color 204
        new Color(0x70, 0x40, 0x80), // color 205
        new Color(0x39, 0x00, 0x4c), // color 206
        new Color(0x43, 0x26, 0x4c), // color 207
        new Color(0x1d, 0x00, 0x26), // color 208
        new Color(0x21, 0x13, 0x26), // color 209
        new Color(0xff, 0x00, 0xff), // color 210
        new Color(0xff, 0x80, 0xff), // color 211
        new Color(0xa6, 0x00, 0xa6), // color 212
        new Color(0xa6, 0x53, 0xa6), // color 213
        new Color(0x80, 0x00, 0x80), // color 214
        new Color(0x80, 0x40, 0x80), // color 215
        new Color(0x4c, 0x00, 0x4c), // color 216
        new Color(0x4c, 0x26, 0x4c), // color 217
        new Color(0x26, 0x00, 0x26), // color 218
        new Color(0x26, 0x13, 0x26), // color 219
        new Color(0xff, 0x00, 0xbf), // color 220
        new Color(0xff, 0x80, 0xdf), // color 221
        new Color(0xa6, 0x00, 0x7c), // color 222
        new Color(0xa6, 0x53, 0x91), // color 223
        new Color(0x80, 0x00, 0x60), // color 224
        new Color(0x80, 0x40, 0x70), // color 225
        new Color(0x4c, 0x00, 0x39), // color 226
        new Color(0x4c, 0x26, 0x43), // color 227
        new Color(0x26, 0x00, 0x1d), // color 228
        new Color(0x26, 0x13, 0x21), // color 229
        new Color(0xff, 0x00, 0x80), // color 230
        new Color(0xff, 0x80, 0xbf), // color 231
        new Color(0xa6, 0x00, 0x53), // color 232
        new Color(0xa6, 0x53, 0x7c), // color 233
        new Color(0x80, 0x00, 0x40), // color 234
        new Color(0x80, 0x40, 0x60), // color 235
        new Color(0x4c, 0x00, 0x26), // color 236
        new Color(0x4c, 0x26, 0x39), // color 237
        new Color(0x26, 0x00, 0x13), // color 238
        new Color(0x26, 0x13, 0x1d), // color 239
        new Color(0xff, 0x00, 0x40), // color 240
        new Color(0xff, 0x80, 0x9f), // color 241
        new Color(0xa6, 0x00, 0x29), // color 242
        new Color(0xa6, 0x53, 0x68), // color 243
        new Color(0x80, 0x00, 0x20), // color 244
        new Color(0x80, 0x40, 0x50), // color 245
        new Color(0x4c, 0x00, 0x13), // color 246
        new Color(0x4c, 0x26, 0x30), // color 247
        new Color(0x26, 0x00, 0x0a), // color 248
        new Color(0x26, 0x13, 0x18), // color 249
        new Color(0x54, 0x54, 0x54), // color 250
        new Color(0x76, 0x76, 0x76), // color 251
        new Color(0x98, 0x98, 0x98), // color 252
        new Color(0xbb, 0xbb, 0xbb), // color 253
        new Color(0xdd, 0xdd, 0xdd), // color 254
        new Color(0x00, 0x00, 0x00)      // color 0	bylayer
    };

    public static Color getDefaultColor() {
        return ColorMap[defaultColor];
    }

    public static int getDefaultColorIndex() {
        return defaultColor;
    }

    public static void setDefaultColor(int n) {
        defaultColor = n;
    }

    public static Color getSelectingColor() {
        return ColorMap[2];
    }

    public static Color getChangingColor() {
        return ColorMap[3];
    }

    public final static Color getColor(int index) {
        if ((index >= 0) && (index <= 255)) {
            return ColorMap[index];
        } else {
            return null;
        }
    }

    public final static int getColor(Color c) {
        if (c == null) {
            return -1;
        }
        for (int i = ColorMap.length - 1; i >= 0; i--) {
            if (ColorMap[i].equals(c)) {
                return i;
            }
        }
        return defaultColor;
    }

    public final static String getColorRGB(int index) {
        Color cc = getColor(index);
        if (cc == null) {
            return null;
        }
        String red = Integer.toString(cc.getRed());
        String green = Integer.toString(cc.getGreen());
        String blue = Integer.toString(cc.getBlue());
        return red + " " + green + " " + blue;
    }
}
