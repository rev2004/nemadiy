var data = [
[{o: 0.567, f: 27.369, l: "A", a: 0},
{o: 27.369, f: 45.711, l: "A", a: 1},
{o: 45.711, f: 64.27, l: "A", a: 0},
{o: 64.27, f: 82.457, l: "A", a: 1},
{o: 82.457, f: 101.334, l: "B", a: 0},
{o: 101.334, f: 138.128, l: "B", a: 1},
{o: 138.128, f: 174.855, l: "B", a: 0},
{o: 174.855, f: 198.933, l: "B", a: 1}],
[{o: 0.424, f: 8.076, l: "E", a: 0},
{o: 8.076, f: 26.116, l: "E", a: 1},
{o: 26.116, f: 42, l: "E", a: 0},
{o: 42, f: 56.404, l: "E", a: 1},
{o: 56.404, f: 67.08, l: "E", a: 0},
{o: 67.08, f: 83.66, l: "E", a: 1},
{o: 83.66, f: 99.38, l: "E", a: 0},
{o: 99.38, f: 108.744, l: "E", a: 1},
{o: 108.744, f: 120.844, l: "E", a: 0},
{o: 120.844, f: 134.592, l: "A", a: 1},
{o: 134.592, f: 149.844, l: "A", a: 0},
{o: 149.844, f: 166.112, l: "A", a: 1},
{o: 166.112, f: 175.104, l: "E", a: 0},
{o: 175.104, f: 196.156, l: "A", a: 1}],
[{o: 0.424, f: 8.076, l: "E", a: 0},
{o: 8.076, f: 26.116, l: "C", a: 1},
{o: 26.116, f: 42, l: "E", a: 0},
{o: 42, f: 56.404, l: "C", a: 1},
{o: 56.404, f: 67.08, l: "C", a: 0},
{o: 67.08, f: 83.66, l: "C", a: 1},
{o: 83.66, f: 99.38, l: "D", a: 0},
{o: 99.38, f: 108.744, l: "D", a: 1},
{o: 108.744, f: 120.844, l: "F", a: 0},
{o: 120.844, f: 134.592, l: "A", a: 1},
{o: 134.592, f: 149.844, l: "A", a: 0},
{o: 149.844, f: 166.112, l: "B", a: 1},
{o: 166.112, f: 175.104, l: "G", a: 0},
{o: 175.104, f: 196.156, l: "B", a: 1}],
[{o: 0.733, f: 3.28, l: "8", a: 0},
{o: 3.28, f: 9.52, l: "3", a: 1},
{o: 9.52, f: 21.893, l: "2", a: 0},
{o: 21.893, f: 27.6, l: "3", a: 1},
{o: 27.6, f: 39.147, l: "2", a: 0},
{o: 39.147, f: 45.453, l: "3", a: 1},
{o: 45.453, f: 51.787, l: "2", a: 0},
{o: 51.787, f: 64.52, l: "5", a: 1},
{o: 64.52, f: 76.453, l: "2", a: 0},
{o: 76.453, f: 83.387, l: "3", a: 1},
{o: 83.387, f: 133.173, l: "1", a: 0},
{o: 133.173, f: 139.547, l: "6", a: 1},
{o: 139.547, f: 152.72, l: "1", a: 0},
{o: 152.72, f: 163.547, l: "4", a: 1},
{o: 163.547, f: 171, l: "1", a: 0},
{o: 171, f: 193.027, l: "4", a: 1},
{o: 193.027, f: 198.573, l: "7", a: 0}],
[{o: 0, f: 28.31, l: "a", a: 0},
{o: 28.31, f: 55.875, l: "b", a: 1},
{o: 55.875, f: 82.695, l: "a", a: 0},
{o: 82.695, f: 99.085, l: "c", a: 1},
{o: 99.085, f: 116.965, l: "c", a: 0},
{o: 116.965, f: 136.335, l: "d", a: 1},
{o: 136.335, f: 152.725, l: "c", a: 0},
{o: 152.725, f: 170.605, l: "c", a: 1},
{o: 170.605, f: 198.17, l: "d", a: 0}],
[{o: 0, f: 5.596, l: "n1", a: 0},
{o: 5.596, f: 14.362, l: "D", a: 1},
{o: 14.362, f: 23.615, l: "C", a: 0},
{o: 23.615, f: 41.993, l: "B", a: 1},
{o: 41.993, f: 60.442, l: "B", a: 0},
{o: 60.442, f: 69.404, l: "D", a: 1},
{o: 69.404, f: 78.472, l: "C", a: 0},
{o: 78.472, f: 83.685, l: "n5", a: 1},
{o: 83.685, f: 97.396, l: "A", a: 0},
{o: 97.396, f: 101.959, l: "n6", a: 1},
{o: 101.959, f: 115.693, l: "A", a: 0},
{o: 115.693, f: 120.314, l: "n7", a: 1},
{o: 120.314, f: 134.049, l: "A", a: 0},
{o: 134.049, f: 138.704, l: "n8", a: 1},
{o: 138.704, f: 152.474, l: "A", a: 0},
{o: 152.474, f: 157.037, l: "n9", a: 1},
{o: 157.037, f: 170.736, l: "A", a: 0},
{o: 170.736, f: 175.427, l: "n10", a: 1},
{o: 175.427, f: 189.231, l: "A", a: 0},
{o: 189.231, f: 198.809, l: "n11", a: 1}],
[{o: 0, f: 0.428, l: "F", a: 0},
{o: 0.428, f: 26.408, l: "E", a: 1},
{o: 26.408, f: 64.512, l: "C", a: 0},
{o: 64.512, f: 75.332, l: "E", a: 1},
{o: 75.332, f: 89.676, l: "B", a: 0},
{o: 89.676, f: 180.276, l: "D", a: 1},
{o: 180.276, f: 196.152, l: "A", a: 0},
{o: 196.152, f: 198.924, l: "F", a: 1}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami001204.ogg";

var artist = "King Oliver";

var track = "Snake Rag";
