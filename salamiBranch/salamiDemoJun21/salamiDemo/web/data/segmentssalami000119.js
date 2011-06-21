var data = [
[{o: 0.041, f: 5.478, l: "Z", a: 0},
{o: 5.478, f: 24.498, l: "A", a: 1},
{o: 24.498, f: 44.068, l: "B", a: 0},
{o: 44.068, f: 63.352, l: "C", a: 1},
{o: 63.352, f: 82.6, l: "B", a: 0},
{o: 82.6, f: 101.552, l: "C", a: 1},
{o: 101.552, f: 120.82, l: "B", a: 0},
{o: 120.82, f: 149.498, l: "C", a: 1},
{o: 149.498, f: 187.963, l: "C", a: 0},
{o: 187.963, f: 263.877, l: "C", a: 1},
{o: 263.877, f: 288.16, l: "Z", a: 0}],
[{o: 0.04, f: 12.488, l: "C", a: 0},
{o: 12.488, f: 25.292, l: "C", a: 1},
{o: 25.292, f: 32.816, l: "C", a: 0},
{o: 32.816, f: 42.336, l: "C", a: 1},
{o: 42.336, f: 61.372, l: "C", a: 0},
{o: 61.372, f: 81.708, l: "C", a: 1},
{o: 81.708, f: 102.656, l: "C", a: 0},
{o: 102.656, f: 113.34, l: "C", a: 1},
{o: 113.34, f: 127.176, l: "C", a: 0},
{o: 127.176, f: 149.564, l: "C", a: 1},
{o: 149.564, f: 166.484, l: "C", a: 0},
{o: 166.484, f: 185.7, l: "C", a: 1},
{o: 185.7, f: 195.1, l: "C", a: 0},
{o: 195.1, f: 201.972, l: "C", a: 1},
{o: 201.972, f: 211.12, l: "A", a: 0},
{o: 211.12, f: 226.68, l: "C", a: 1},
{o: 226.68, f: 241.444, l: "C", a: 0},
{o: 241.444, f: 263.032, l: "C", a: 1},
{o: 263.032, f: 270.216, l: "C", a: 0},
{o: 270.216, f: 280.776, l: "C", a: 1},
{o: 280.776, f: 287.94, l: "C", a: 0}],
[{o: 0.04, f: 12.488, l: "C", a: 0},
{o: 12.488, f: 25.292, l: "C", a: 1},
{o: 25.292, f: 32.816, l: "A", a: 0},
{o: 32.816, f: 42.336, l: "C", a: 1},
{o: 42.336, f: 61.372, l: "C", a: 0},
{o: 61.372, f: 81.708, l: "C", a: 1},
{o: 81.708, f: 102.656, l: "C", a: 0},
{o: 102.656, f: 113.34, l: "C", a: 1},
{o: 113.34, f: 127.176, l: "C", a: 0},
{o: 127.176, f: 149.564, l: "C", a: 1},
{o: 149.564, f: 166.484, l: "D", a: 0},
{o: 166.484, f: 185.7, l: "B", a: 1},
{o: 185.7, f: 195.1, l: "B", a: 0},
{o: 195.1, f: 201.972, l: "B", a: 1},
{o: 201.972, f: 211.12, l: "E", a: 0},
{o: 211.12, f: 226.68, l: "B", a: 1},
{o: 226.68, f: 241.444, l: "F", a: 0},
{o: 241.444, f: 263.032, l: "G", a: 1},
{o: 263.032, f: 270.216, l: "H", a: 0},
{o: 270.216, f: 280.776, l: "I", a: 1},
{o: 280.776, f: 287.94, l: "J", a: 0}],
[{o: 0.6, f: 5.067, l: "8", a: 0},
{o: 5.067, f: 24.773, l: "2", a: 1},
{o: 24.773, f: 42.933, l: "1", a: 0},
{o: 42.933, f: 62.893, l: "2", a: 1},
{o: 62.893, f: 80.827, l: "1", a: 0},
{o: 80.827, f: 99.987, l: "2", a: 1},
{o: 99.987, f: 120.867, l: "1", a: 0},
{o: 120.867, f: 147.773, l: "5", a: 1},
{o: 147.773, f: 167.68, l: "2", a: 0},
{o: 167.68, f: 186.28, l: "3", a: 1},
{o: 186.28, f: 195.387, l: "4", a: 0},
{o: 195.387, f: 208.453, l: "1", a: 1},
{o: 208.453, f: 215.04, l: "4", a: 0},
{o: 215.04, f: 222.827, l: "1", a: 1},
{o: 222.827, f: 261.933, l: "3", a: 0},
{o: 261.933, f: 271.16, l: "7", a: 1},
{o: 271.16, f: 287.88, l: "6", a: 0}],
[{o: 0, f: 145.275, l: "a", a: 0},
{o: 145.275, f: 166.135, l: "b", a: 1},
{o: 166.135, f: 187.74, l: "b", a: 0},
{o: 187.74, f: 213.815, l: "c", a: 1},
{o: 213.815, f: 234.675, l: "b", a: 0},
{o: 234.675, f: 258.515, l: "b", a: 1},
{o: 258.515, f: 287.57, l: "d", a: 0}],
[{o: 0, f: 60.5, l: "n1", a: 0},
{o: 60.5, f: 83.244, l: "A", a: 1},
{o: 83.244, f: 98.813, l: "n2", a: 0},
{o: 98.813, f: 121.51, l: "A", a: 1},
{o: 121.51, f: 197.521, l: "n3", a: 0},
{o: 197.521, f: 207.041, l: "B", a: 1},
{o: 207.041, f: 216.549, l: "B", a: 0},
{o: 216.549, f: 241.882, l: "n4", a: 1},
{o: 241.882, f: 250.95, l: "C", a: 0},
{o: 250.95, f: 258.926, l: "C", a: 1},
{o: 258.926, f: 287.974, l: "n6", a: 0}],
[{o: 0, f: 0.04, l: "E", a: 0},
{o: 0.04, f: 0.04, l: "B", a: 1},
{o: 0.04, f: 24.988, l: "A", a: 0},
{o: 24.988, f: 45.04, l: "C", a: 1},
{o: 45.04, f: 65.884, l: "D", a: 0},
{o: 65.884, f: 81.424, l: "C", a: 1},
{o: 81.424, f: 102.056, l: "A", a: 0},
{o: 102.056, f: 125.66, l: "C", a: 1},
{o: 125.66, f: 184.204, l: "A", a: 0},
{o: 184.204, f: 224.06, l: "D", a: 1},
{o: 224.06, f: 276.636, l: "A", a: 0},
{o: 276.636, f: 287.936, l: "B", a: 1},
{o: 287.936, f: 288, l: "E", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000119.ogg";

var artist = "The Alchemystics";

var track = "I Got Your Number";
