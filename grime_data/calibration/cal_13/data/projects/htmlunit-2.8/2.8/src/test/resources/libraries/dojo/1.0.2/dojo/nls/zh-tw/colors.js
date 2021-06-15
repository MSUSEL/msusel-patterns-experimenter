/*
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
({"lightsteelblue": "淡鐵藍色", "orangered": "橙紅色", "midnightblue": "午夜藍", "cadetblue": "軍服藍", "seashell": "海貝色", "slategrey": "岩灰色", "coral": "珊瑚紅", "darkturquoise": "暗松石綠", "antiquewhite": "米白色", "mediumspringgreen": "中春綠色", "salmon": "鮭紅色", "darkgrey": "暗灰色", "ivory": "象牙色", "greenyellow": "綠黃色", "mistyrose": "霧玫瑰色", "lightsalmon": "淡鮭紅", "silver": "銀色", "dimgrey": "昏灰色", "orange": "橙色", "white": "白色", "navajowhite": "印地安黃色", "royalblue": "品藍色", "deeppink": "深粉紅色", "lime": "檸檬色", "oldlace": "舊蕾絲色", "chartreuse": "淡黃綠色", "darkcyan": "暗青色", "yellow": "黃色", "linen": "亞麻色", "olive": "橄欖色", "gold": "金色", "lawngreen": "草綠色", "lightyellow": "淡黃色", "tan": "皮革色", "darkviolet": "暗紫羅蘭色", "lightslategrey": "淡岩灰色", "grey": "灰色", "darkkhaki": "暗卡其色", "green": "綠色", "deepskyblue": "深天藍色", "aqua": "水色", "sienna": "黃土赭色", "mintcream": "薄荷乳白色", "rosybrown": "玫瑰褐", "mediumslateblue": "中岩藍色", "magenta": "紫紅色", "lightseagreen": "淡海綠色", "cyan": "青色", "olivedrab": "橄欖綠", "darkgoldenrod": "暗金菊色", "slateblue": "岩藍色", "mediumaquamarine": "中碧綠色", "lavender": "薰衣草紫", "mediumseagreen": "中海綠色", "maroon": "栗色", "darkslategray": "暗岩灰色", "mediumturquoise": "中松石綠", "ghostwhite": "幽靈色", "darkblue": "暗藍色", "mediumvioletred": "中紫羅蘭紅", "brown": "褐色", "lightgray": "淡灰色", "sandybrown": "沙褐色", "pink": "粉紅色", "firebrick": "紅磚色", "indigo": "靛藍色", "snow": "雪白色", "darkorchid": "暗蘭花色", "turquoise": "松石綠", "chocolate": "巧克力色", "springgreen": "春綠色", "moccasin": "鹿皮黃色", "navy": "海軍藍", "lemonchiffon": "奶油黃", "teal": "深藍綠色", "floralwhite": "花卉白", "cornflowerblue": "矢車菊藍", "paleturquoise": "灰松石綠", "purple": "紫色", "gainsboro": "石板灰", "plum": "李紫色", "red": "紅色", "blue": "藍色", "forestgreen": "森綠色", "darkgreen": "暗綠色", "honeydew": "密瓜色", "darkseagreen": "暗海綠色", "lightcoral": "淡珊瑚紅", "palevioletred": "灰紫羅蘭紅", "mediumpurple": "中紫色", "saddlebrown": "鞍褐色", "darkmagenta": "暗紫紅色", "thistle": "薊色", "whitesmoke": "白煙色", "wheat": "小麥色", "violet": "紫羅蘭色", "lightskyblue": "淡天藍色", "goldenrod": "金菊色", "mediumblue": "中藍色", "skyblue": "天藍色", "crimson": "暗深紅色", "darksalmon": "暗鮭紅", "darkred": "暗紅色", "darkslategrey": "暗岩灰色", "peru": "祕魯色", "lightgrey": "淡灰色", "lightgoldenrodyellow": "淡金菊黃", "blanchedalmond": "杏仁白", "aliceblue": "愛麗絲藍", "bisque": "橘黃色", "slategray": "岩灰色", "palegoldenrod": "灰金菊色", "darkorange": "暗橙色", "aquamarine": "碧綠色", "lightgreen": "淡綠色", "burlywood": "實木色", "dodgerblue": "道奇藍", "darkgray": "暗灰色", "lightcyan": "淡青色", "powderblue": "粉藍色", "blueviolet": "藍紫色", "orchid": "蘭花色", "dimgray": "昏灰色", "beige": "灰棕色", "fuchsia": "海棠紅", "lavenderblush": "薰衣草紫紅", "hotpink": "暖粉紅色", "steelblue": "鐵藍色", "tomato": "蕃茄紅", "lightpink": "淡粉紅色", "limegreen": "檸檬綠", "indianred": "印度紅", "papayawhip": "番木瓜色", "lightslategray": "淡岩灰色", "gray": "灰色", "mediumorchid": "中蘭紫色", "cornsilk": "玉米黃", "black": "黑色", "seagreen": "海綠色", "darkslateblue": "暗岩藍色", "khaki": "卡其色", "lightblue": "淡藍色", "palegreen": "灰綠色", "azure": "天藍色", "peachpuff": "粉撲桃色", "darkolivegreen": "暗橄欖綠", "yellowgreen": "黃綠色"})