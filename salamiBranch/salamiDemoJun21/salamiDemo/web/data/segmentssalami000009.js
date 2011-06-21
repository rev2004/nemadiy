var data = [
[{o: 0.882, f: 16.228, l: "A", a: 0},
{o: 16.228, f: 31.275, l: "B", a: 1},
{o: 31.275, f: 48.145, l: "B'", a: 0},
{o: 48.145, f: 63.235, l: "D", a: 1},
{o: 63.235, f: 80.484, l: "D'", a: 0},
{o: 80.484, f: 95.802, l: "B", a: 1},
{o: 95.802, f: 112.89, l: "B", a: 0},
{o: 112.89, f: 128.145, l: "B", a: 1},
{o: 128.145, f: 145.194, l: "B'", a: 0},
{o: 145.194, f: 160.639, l: "C", a: 1},
{o: 160.639, f: 290.266, l: "C", a: 0}],
[{o: 0.068, f: 15.664, l: "B", a: 0},
{o: 15.664, f: 28.864, l: "B", a: 1},
{o: 28.864, f: 51.404, l: "B", a: 0},
{o: 51.404, f: 59.924, l: "B", a: 1},
{o: 59.924, f: 67.988, l: "B", a: 0},
{o: 67.988, f: 80.924, l: "B", a: 1},
{o: 80.924, f: 93.816, l: "B", a: 0},
{o: 93.816, f: 109.036, l: "B", a: 1},
{o: 109.036, f: 130.932, l: "B", a: 0},
{o: 130.932, f: 147.152, l: "B", a: 1},
{o: 147.152, f: 154.888, l: "E", a: 0},
{o: 154.888, f: 164.42, l: "E", a: 1},
{o: 164.42, f: 185.36, l: "E", a: 0},
{o: 185.36, f: 196.152, l: "E", a: 1},
{o: 196.152, f: 206.876, l: "E", a: 0},
{o: 206.876, f: 217.596, l: "B", a: 1},
{o: 217.596, f: 225.924, l: "E", a: 0},
{o: 225.924, f: 233.452, l: "E", a: 1},
{o: 233.452, f: 243.768, l: "E", a: 0},
{o: 243.768, f: 249.764, l: "E", a: 1},
{o: 249.764, f: 259.672, l: "E", a: 0},
{o: 259.672, f: 273.02, l: "E", a: 1},
{o: 273.02, f: 290.12, l: "E", a: 0}],
[{o: 0.068, f: 15.664, l: "B", a: 0},
{o: 15.664, f: 28.864, l: "B", a: 1},
{o: 28.864, f: 51.404, l: "B", a: 0},
{o: 51.404, f: 59.924, l: "B", a: 1},
{o: 59.924, f: 67.988, l: "B", a: 0},
{o: 67.988, f: 80.924, l: "B", a: 1},
{o: 80.924, f: 93.816, l: "B", a: 0},
{o: 93.816, f: 109.036, l: "B", a: 1},
{o: 109.036, f: 130.932, l: "B", a: 0},
{o: 130.932, f: 147.152, l: "B", a: 1},
{o: 147.152, f: 154.888, l: "E", a: 0},
{o: 154.888, f: 164.42, l: "F", a: 1},
{o: 164.42, f: 185.36, l: "G", a: 0},
{o: 185.36, f: 196.152, l: "H", a: 1},
{o: 196.152, f: 206.876, l: "C", a: 0},
{o: 206.876, f: 217.596, l: "I", a: 1},
{o: 217.596, f: 225.924, l: "D", a: 0},
{o: 225.924, f: 233.452, l: "A", a: 1},
{o: 233.452, f: 243.768, l: "D", a: 0},
{o: 243.768, f: 249.764, l: "C", a: 1},
{o: 249.764, f: 259.672, l: "J", a: 0},
{o: 259.672, f: 273.02, l: "K", a: 1},
{o: 273.02, f: 290.12, l: "A", a: 0}],
[{o: 0.493, f: 18.733, l: "1", a: 0},
{o: 18.733, f: 23.453, l: "8", a: 1},
{o: 23.453, f: 47.387, l: "1", a: 0},
{o: 47.387, f: 68.76, l: "2", a: 1},
{o: 68.76, f: 111.64, l: "1", a: 0},
{o: 111.64, f: 116.933, l: "4", a: 1},
{o: 116.933, f: 126.44, l: "1", a: 0},
{o: 126.44, f: 133.613, l: "4", a: 1},
{o: 133.613, f: 139.773, l: "1", a: 0},
{o: 139.773, f: 149.4, l: "2", a: 1},
{o: 149.4, f: 159.413, l: "1", a: 0},
{o: 159.413, f: 163.747, l: "2", a: 1},
{o: 163.747, f: 176.093, l: "1", a: 0},
{o: 176.093, f: 181.72, l: "7", a: 1},
{o: 181.72, f: 258.2, l: "3", a: 0},
{o: 258.2, f: 274.24, l: "5", a: 1},
{o: 274.24, f: 290.04, l: "6", a: 0}],
[{o: 0, f: 19.37, l: "a", a: 0},
{o: 19.37, f: 70.03, l: "b", a: 1},
{o: 70.03, f: 94.615, l: "c", a: 0},
{o: 94.615, f: 116.22, l: "c", a: 1},
{o: 116.22, f: 167.625, l: "b", a: 0},
{o: 167.625, f: 289.805, l: "d", a: 1}],
[{o: 0, f: 52.86, l: "n1", a: 0},
{o: 52.86, f: 69.95, l: "A", a: 1},
{o: 69.95, f: 85.24, l: "n2", a: 0},
{o: 85.24, f: 102.435, l: "A", a: 1},
{o: 102.435, f: 150.117, l: "n3", a: 0},
{o: 150.117, f: 167.323, l: "A", a: 1},
{o: 167.323, f: 289.831, l: "n4", a: 0}],
[{o: 0, f: 0.068, l: "G", a: 0},
{o: 0.068, f: 13.436, l: "C", a: 1},
{o: 13.436, f: 164.42, l: "B", a: 0},
{o: 164.42, f: 192.828, l: "A", a: 1},
{o: 192.828, f: 218.056, l: "B", a: 0},
{o: 218.056, f: 262.648, l: "E", a: 1},
{o: 262.648, f: 289.692, l: "F", a: 0},
{o: 289.692, f: 290.12, l: "E", a: 1},
{o: 290.12, f: 290.157, l: "G", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000009.ogg";

var artist = "40 Rod Lightning";

var track = "Tip My Glass";
