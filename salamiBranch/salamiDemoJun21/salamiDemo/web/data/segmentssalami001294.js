var data = [
[{o: 0.405, f: 8.524, l: "A", a: 0},
{o: 8.524, f: 42.896, l: "B", a: 1},
{o: 42.896, f: 63.215, l: "C", a: 0},
{o: 63.215, f: 83.344, l: "D", a: 1},
{o: 83.344, f: 140.711, l: "E", a: 0},
{o: 140.711, f: 154.965, l: "B", a: 1},
{o: 154.965, f: 170.353, l: "C", a: 0},
{o: 170.353, f: 189.988, l: "D", a: 1},
{o: 189.988, f: 228.101, l: "E", a: 0}],
[{o: 0.304, f: 11.36, l: "D", a: 0},
{o: 11.36, f: 22.832, l: "A", a: 1},
{o: 22.832, f: 42.112, l: "A", a: 0},
{o: 42.112, f: 59.02, l: "D", a: 1},
{o: 59.02, f: 73.596, l: "D", a: 0},
{o: 73.596, f: 83.848, l: "D", a: 1},
{o: 83.848, f: 107.392, l: "D", a: 0},
{o: 107.392, f: 119.484, l: "D", a: 1},
{o: 119.484, f: 133.324, l: "B", a: 0},
{o: 133.324, f: 138.928, l: "D", a: 1},
{o: 138.928, f: 156.888, l: "A", a: 0},
{o: 156.888, f: 167.212, l: "D", a: 1},
{o: 167.212, f: 180.676, l: "B", a: 0},
{o: 180.676, f: 190.268, l: "D", a: 1},
{o: 190.268, f: 204.272, l: "D", a: 0},
{o: 204.272, f: 221.244, l: "C", a: 1}],
[{o: 0.304, f: 11.36, l: "D", a: 0},
{o: 11.36, f: 22.832, l: "A", a: 1},
{o: 22.832, f: 42.112, l: "A", a: 0},
{o: 42.112, f: 59.02, l: "D", a: 1},
{o: 59.02, f: 73.596, l: "C", a: 0},
{o: 73.596, f: 83.848, l: "E", a: 1},
{o: 83.848, f: 107.392, l: "B", a: 0},
{o: 107.392, f: 119.484, l: "B", a: 1},
{o: 119.484, f: 133.324, l: "F", a: 0},
{o: 133.324, f: 138.928, l: "C", a: 1},
{o: 138.928, f: 156.888, l: "A", a: 0},
{o: 156.888, f: 167.212, l: "C", a: 1},
{o: 167.212, f: 180.676, l: "G", a: 0},
{o: 180.676, f: 190.268, l: "H", a: 1},
{o: 190.268, f: 204.272, l: "B", a: 0},
{o: 204.272, f: 221.244, l: "I", a: 1}],
[{o: 0.387, f: 1.533, l: "8", a: 0},
{o: 1.533, f: 8.6, l: "1", a: 1},
{o: 8.6, f: 15.627, l: "3", a: 0},
{o: 15.627, f: 22.88, l: "2", a: 1},
{o: 22.88, f: 33.72, l: "3", a: 0},
{o: 33.72, f: 42.32, l: "1", a: 1},
{o: 42.32, f: 53.2, l: "2", a: 0},
{o: 53.2, f: 68.373, l: "1", a: 1},
{o: 68.373, f: 76.787, l: "5", a: 0},
{o: 76.787, f: 84.333, l: "1", a: 1},
{o: 84.333, f: 95, l: "2", a: 0},
{o: 95, f: 100.693, l: "7", a: 1},
{o: 100.693, f: 133.12, l: "4", a: 0},
{o: 133.12, f: 140.827, l: "1", a: 1},
{o: 140.827, f: 149.28, l: "3", a: 0},
{o: 149.28, f: 154.16, l: "1", a: 1},
{o: 154.16, f: 163.627, l: "2", a: 0},
{o: 163.627, f: 176, l: "1", a: 1},
{o: 176, f: 183.76, l: "5", a: 0},
{o: 183.76, f: 190.973, l: "1", a: 1},
{o: 190.973, f: 203.467, l: "2", a: 0},
{o: 203.467, f: 210.08, l: "1", a: 1},
{o: 210.08, f: 217.693, l: "3", a: 0},
{o: 217.693, f: 228.013, l: "6", a: 1}],
[{o: 0, f: 21.605, l: "a", a: 0},
{o: 21.605, f: 70.03, l: "b", a: 1},
{o: 70.03, f: 96.105, l: "a", a: 0},
{o: 96.105, f: 134.845, l: "c", a: 1},
{o: 134.845, f: 177.31, l: "b", a: 0},
{o: 177.31, f: 202.64, l: "a", a: 1},
{o: 202.64, f: 227.225, l: "d", a: 0}],
[{o: 0, f: 1.3, l: "n1", a: 0},
{o: 1.3, f: 15.337, l: "A", a: 1},
{o: 15.337, f: 26.424, l: "n2", a: 0},
{o: 26.424, f: 39.636, l: "A", a: 1},
{o: 39.636, f: 59.849, l: "C", a: 0},
{o: 59.849, f: 71.877, l: "n3", a: 1},
{o: 71.877, f: 96.502, l: "B", a: 0},
{o: 96.502, f: 136.208, l: "n4", a: 1},
{o: 136.208, f: 151.754, l: "A", a: 0},
{o: 151.754, f: 170.701, l: "C", a: 1},
{o: 170.701, f: 179.049, l: "n6", a: 0},
{o: 179.049, f: 203.72, l: "B", a: 1},
{o: 203.72, f: 216.062, l: "A", a: 0},
{o: 216.062, f: 227.788, l: "n8", a: 1}],
[{o: 0, f: 0.304, l: "J", a: 0},
{o: 0.304, f: 1.932, l: "C", a: 1},
{o: 1.932, f: 8.564, l: "I", a: 0},
{o: 8.564, f: 18.064, l: "D", a: 1},
{o: 18.064, f: 36.772, l: "A", a: 0},
{o: 36.772, f: 63.424, l: "I", a: 1},
{o: 63.424, f: 77.648, l: "B", a: 0},
{o: 77.648, f: 83.228, l: "I", a: 1},
{o: 83.228, f: 95.156, l: "A", a: 0},
{o: 95.156, f: 118.576, l: "G", a: 1},
{o: 118.576, f: 159.632, l: "I", a: 0},
{o: 159.632, f: 170.784, l: "C", a: 1},
{o: 170.784, f: 189.964, l: "B", a: 0},
{o: 189.964, f: 214.952, l: "A", a: 1},
{o: 214.952, f: 221.244, l: "C", a: 0},
{o: 221.244, f: 228.102, l: "J", a: 1}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami001294.ogg";

var artist = "Compilations";

var track = "Sevilla";
