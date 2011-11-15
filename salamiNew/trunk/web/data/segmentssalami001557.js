var data = [
[{o: 0.013, f: 19.956, l: "A", a: 0},
{o: 19.956, f: 44.287, l: "B", a: 1},
{o: 44.287, f: 69.592, l: "B", a: 0},
{o: 69.592, f: 95.965, l: "C", a: 1},
{o: 95.965, f: 128.472, l: "B", a: 0},
{o: 128.472, f: 134.709, l: "D", a: 1},
{o: 134.709, f: 163.022, l: "B", a: 0},
{o: 163.022, f: 193.284, l: "B", a: 1},
{o: 193.284, f: 224.024, l: "C", a: 0},
{o: 224.024, f: 256.138, l: "B", a: 1},
{o: 256.138, f: 295.675, l: "E", a: 0},
{o: 295.675, f: 301.386, l: "Z", a: 1}],
[{o: 0.18, f: 8.16, l: "D", a: 0},
{o: 8.16, f: 19.048, l: "D", a: 1},
{o: 19.048, f: 31.144, l: "D", a: 0},
{o: 31.144, f: 43.32, l: "D", a: 1},
{o: 43.32, f: 51.332, l: "D", a: 0},
{o: 51.332, f: 61.936, l: "D", a: 1},
{o: 61.936, f: 68.44, l: "D", a: 0},
{o: 68.44, f: 80.152, l: "A", a: 1},
{o: 80.152, f: 94.936, l: "D", a: 0},
{o: 94.936, f: 111.38, l: "D", a: 1},
{o: 111.38, f: 121.38, l: "D", a: 0},
{o: 121.38, f: 128.228, l: "D", a: 1},
{o: 128.228, f: 134.476, l: "D", a: 0},
{o: 134.476, f: 154.304, l: "D", a: 1},
{o: 154.304, f: 163.3, l: "D", a: 0},
{o: 163.3, f: 184.376, l: "D", a: 1},
{o: 184.376, f: 200.944, l: "D", a: 0},
{o: 200.944, f: 216.528, l: "D", a: 1},
{o: 216.528, f: 229.688, l: "D", a: 0},
{o: 229.688, f: 248.384, l: "D", a: 1},
{o: 248.384, f: 258.472, l: "D", a: 0},
{o: 258.472, f: 269.56, l: "D", a: 1},
{o: 269.56, f: 277.336, l: "D", a: 0},
{o: 277.336, f: 292.148, l: "D", a: 1},
{o: 292.148, f: 301.256, l: "D", a: 0}],
[{o: 0.18, f: 8.16, l: "D", a: 0},
{o: 8.16, f: 19.048, l: "E", a: 1},
{o: 19.048, f: 31.144, l: "A", a: 0},
{o: 31.144, f: 43.32, l: "E", a: 1},
{o: 43.32, f: 51.332, l: "F", a: 0},
{o: 51.332, f: 61.936, l: "C", a: 1},
{o: 61.936, f: 68.44, l: "A", a: 0},
{o: 68.44, f: 80.152, l: "G", a: 1},
{o: 80.152, f: 94.936, l: "C", a: 0},
{o: 94.936, f: 111.38, l: "H", a: 1},
{o: 111.38, f: 121.38, l: "I", a: 0},
{o: 121.38, f: 128.228, l: "J", a: 1},
{o: 128.228, f: 134.476, l: "B", a: 0},
{o: 134.476, f: 154.304, l: "B", a: 1},
{o: 154.304, f: 163.3, l: "B", a: 0},
{o: 163.3, f: 184.376, l: "B", a: 1},
{o: 184.376, f: 200.944, l: "B", a: 0},
{o: 200.944, f: 216.528, l: "K", a: 1},
{o: 216.528, f: 229.688, l: "C", a: 0},
{o: 229.688, f: 248.384, l: "B", a: 1},
{o: 248.384, f: 258.472, l: "C", a: 0},
{o: 258.472, f: 269.56, l: "D", a: 1},
{o: 269.56, f: 277.336, l: "C", a: 0},
{o: 277.336, f: 292.148, l: "B", a: 1},
{o: 292.148, f: 301.256, l: "B", a: 0}],
[{o: 0.653, f: 18.987, l: "3", a: 0},
{o: 18.987, f: 31.373, l: "6", a: 1},
{o: 31.373, f: 38.36, l: "1", a: 0},
{o: 38.36, f: 45.187, l: "3", a: 1},
{o: 45.187, f: 55.693, l: "6", a: 0},
{o: 55.693, f: 61.693, l: "1", a: 1},
{o: 61.693, f: 70.48, l: "3", a: 0},
{o: 70.48, f: 79.733, l: "1", a: 1},
{o: 79.733, f: 87.773, l: "3", a: 0},
{o: 87.773, f: 122.907, l: "1", a: 1},
{o: 122.907, f: 133.973, l: "4", a: 0},
{o: 133.973, f: 145.027, l: "5", a: 1},
{o: 145.027, f: 163.373, l: "2", a: 0},
{o: 163.373, f: 173.707, l: "7", a: 1},
{o: 173.707, f: 189.653, l: "1", a: 0},
{o: 189.653, f: 216.04, l: "2", a: 1},
{o: 216.04, f: 223.787, l: "1", a: 0},
{o: 223.787, f: 236.813, l: "5", a: 1},
{o: 236.813, f: 246.173, l: "1", a: 0},
{o: 246.173, f: 256.973, l: "2", a: 1},
{o: 256.973, f: 265.773, l: "1", a: 0},
{o: 265.773, f: 279.813, l: "4", a: 1},
{o: 279.813, f: 292.987, l: "2", a: 0},
{o: 292.987, f: 301.267, l: "8", a: 1}],
[{o: 0, f: 17.88, l: "a", a: 0},
{o: 17.88, f: 33.525, l: "b", a: 1},
{o: 33.525, f: 58.11, l: "c", a: 0},
{o: 58.11, f: 87.91, l: "d", a: 1},
{o: 87.91, f: 111.75, l: "c", a: 0},
{o: 111.75, f: 125.905, l: "e", a: 1},
{o: 125.905, f: 141.55, l: "b", a: 0},
{o: 141.55, f: 166.135, l: "c", a: 1},
{o: 166.135, f: 195.935, l: "d", a: 0},
{o: 195.935, f: 219.775, l: "c", a: 1},
{o: 219.775, f: 233.93, l: "e", a: 0},
{o: 233.93, f: 262.24, l: "f", a: 1},
{o: 262.24, f: 300.98, l: "g", a: 0}],
[{o: 0, f: 54.776, l: "n1", a: 0},
{o: 54.776, f: 64.877, l: "B", a: 1},
{o: 64.877, f: 96.073, l: "n2", a: 0},
{o: 96.073, f: 106.742, l: "A", a: 1},
{o: 106.742, f: 163.805, l: "n3", a: 0},
{o: 163.805, f: 176.019, l: "A", a: 1},
{o: 176.019, f: 190.427, l: "B", a: 0},
{o: 190.427, f: 224.026, l: "n4", a: 1},
{o: 224.026, f: 238.097, l: "A", a: 0},
{o: 238.097, f: 301.349, l: "n5", a: 1}],
[{o: 0, f: 0.004, l: "J", a: 0},
{o: 0.004, f: 18.088, l: "B", a: 1},
{o: 18.088, f: 34.764, l: "E", a: 0},
{o: 34.764, f: 44.176, l: "A", a: 1},
{o: 44.176, f: 153.508, l: "I", a: 0},
{o: 153.508, f: 163.232, l: "A", a: 1},
{o: 163.232, f: 185.508, l: "E", a: 0},
{o: 185.508, f: 199.1, l: "A", a: 1},
{o: 199.1, f: 205.736, l: "E", a: 0},
{o: 205.736, f: 226.836, l: "I", a: 1},
{o: 226.836, f: 262.68, l: "E", a: 0},
{o: 262.68, f: 268.98, l: "F", a: 1},
{o: 268.98, f: 286.144, l: "A", a: 0},
{o: 286.144, f: 293.916, l: "F", a: 1},
{o: 293.916, f: 301.304, l: "D", a: 0},
{o: 301.304, f: 301.376, l: "J", a: 1}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami001557.ogg";

var artist = "Oliver Jones";

var track = "My Funny Valentine";