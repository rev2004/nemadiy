var data = [
[{o: 1.913, f: 62.603, l: "N", a: 0},
{o: 62.603, f: 92.949, l: "A", a: 1},
{o: 92.949, f: 116.936, l: "A", a: 0},
{o: 116.936, f: 139.506, l: "A", a: 1},
{o: 139.506, f: 161.518, l: "A", a: 0},
{o: 161.518, f: 183.822, l: "A", a: 1},
{o: 183.822, f: 230.012, l: "A", a: 0},
{o: 230.012, f: 288.261, l: "A", a: 1},
{o: 288.261, f: 301.616, l: "A", a: 0},
{o: 301.616, f: 326.495, l: "A", a: 1}],
[{o: 1.228, f: 20.128, l: "D", a: 0},
{o: 20.128, f: 31.744, l: "D", a: 1},
{o: 31.744, f: 45.572, l: "D", a: 0},
{o: 45.572, f: 58.184, l: "D", a: 1},
{o: 58.184, f: 81.564, l: "C", a: 0},
{o: 81.564, f: 106.996, l: "C", a: 1},
{o: 106.996, f: 122.628, l: "C", a: 0},
{o: 122.628, f: 140.408, l: "C", a: 1},
{o: 140.408, f: 158.716, l: "C", a: 0},
{o: 158.716, f: 184.216, l: "C", a: 1},
{o: 184.216, f: 196.588, l: "C", a: 0},
{o: 196.588, f: 226.616, l: "C", a: 1},
{o: 226.616, f: 249.88, l: "C", a: 0},
{o: 249.88, f: 268.116, l: "C", a: 1},
{o: 268.116, f: 288.188, l: "C", a: 0},
{o: 288.188, f: 299.1, l: "C", a: 1},
{o: 299.1, f: 323.06, l: "C", a: 0}],
[{o: 1.228, f: 20.128, l: "D", a: 0},
{o: 20.128, f: 31.744, l: "E", a: 1},
{o: 31.744, f: 45.572, l: "F", a: 0},
{o: 45.572, f: 58.184, l: "G", a: 1},
{o: 58.184, f: 81.564, l: "C", a: 0},
{o: 81.564, f: 106.996, l: "C", a: 1},
{o: 106.996, f: 122.628, l: "B", a: 0},
{o: 122.628, f: 140.408, l: "B", a: 1},
{o: 140.408, f: 158.716, l: "B", a: 0},
{o: 158.716, f: 184.216, l: "B", a: 1},
{o: 184.216, f: 196.588, l: "H", a: 0},
{o: 196.588, f: 226.616, l: "B", a: 1},
{o: 226.616, f: 249.88, l: "B", a: 0},
{o: 249.88, f: 268.116, l: "B", a: 1},
{o: 268.116, f: 288.188, l: "B", a: 0},
{o: 288.188, f: 299.1, l: "A", a: 1},
{o: 299.1, f: 323.06, l: "A", a: 0}],
[{o: 0.44, f: 5.387, l: "7", a: 0},
{o: 5.387, f: 10.973, l: "2", a: 1},
{o: 10.973, f: 29.88, l: "4", a: 0},
{o: 29.88, f: 36.2, l: "2", a: 1},
{o: 36.2, f: 63.147, l: "6", a: 0},
{o: 63.147, f: 69.107, l: "1", a: 1},
{o: 69.107, f: 80.2, l: "3", a: 0},
{o: 80.2, f: 92.293, l: "2", a: 1},
{o: 92.293, f: 102.347, l: "1", a: 0},
{o: 102.347, f: 111.64, l: "3", a: 1},
{o: 111.64, f: 116.6, l: "2", a: 0},
{o: 116.6, f: 134.733, l: "3", a: 1},
{o: 134.733, f: 150.373, l: "1", a: 0},
{o: 150.373, f: 160.587, l: "2", a: 1},
{o: 160.587, f: 183.547, l: "1", a: 0},
{o: 183.547, f: 193.547, l: "5", a: 1},
{o: 193.547, f: 199.36, l: "3", a: 0},
{o: 199.36, f: 207.693, l: "5", a: 1},
{o: 207.693, f: 229.333, l: "1", a: 0},
{o: 229.333, f: 239.373, l: "4", a: 1},
{o: 239.373, f: 252.08, l: "1", a: 0},
{o: 252.08, f: 262.84, l: "3", a: 1},
{o: 262.84, f: 277.253, l: "1", a: 0},
{o: 277.253, f: 287.52, l: "2", a: 1},
{o: 287.52, f: 295.04, l: "3", a: 0},
{o: 295.04, f: 300.613, l: "2", a: 1},
{o: 300.613, f: 305.32, l: "4", a: 0},
{o: 305.32, f: 314.453, l: "1", a: 1},
{o: 314.453, f: 321.987, l: "2", a: 0},
{o: 321.987, f: 326.387, l: "8", a: 1}],
[{o: 0, f: 0.745, l: "a", a: 0},
{o: 0.745, f: 62.58, l: "b", a: 1},
{o: 62.58, f: 95.36, l: "c", a: 0},
{o: 95.36, f: 128.885, l: "c", a: 1},
{o: 128.885, f: 184.015, l: "d", a: 0},
{o: 184.015, f: 210.835, l: "e", a: 1},
{o: 210.835, f: 243.615, l: "c", a: 0},
{o: 243.615, f: 277.14, l: "c", a: 1},
{o: 277.14, f: 324.82, l: "d", a: 0},
{o: 324.82, f: 326.31, l: "a", a: 1}],
[{o: 0, f: 10.019, l: "n1", a: 0},
{o: 10.019, f: 21.269, l: "A", a: 1},
{o: 21.269, f: 27.341, l: "n2", a: 0},
{o: 27.341, f: 42.225, l: "A", a: 1},
{o: 42.225, f: 226.882, l: "n3", a: 0},
{o: 226.882, f: 240.756, l: "A", a: 1},
{o: 240.756, f: 326.24, l: "n4", a: 0}],
[{o: 0, f: 1.2, l: "G", a: 0},
{o: 1.2, f: 62.584, l: "B", a: 1},
{o: 62.584, f: 92.72, l: "E", a: 0},
{o: 92.72, f: 102.596, l: "B", a: 1},
{o: 102.596, f: 186.684, l: "E", a: 0},
{o: 186.684, f: 219.756, l: "C", a: 1},
{o: 219.756, f: 240.396, l: "B", a: 0},
{o: 240.396, f: 250.784, l: "F", a: 1},
{o: 250.784, f: 292.184, l: "E", a: 0},
{o: 292.184, f: 312.6, l: "B", a: 1},
{o: 312.6, f: 323.096, l: "C", a: 0},
{o: 323.096, f: 326.479, l: "G", a: 1}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000652.ogg";

var artist = "Compilations";

var track = "Raga Tilak Kamod Madhyalaya Teentaa";
