var data = [
[{o: 2.188, f: 12.863, l: "A", a: 0},
{o: 12.863, f: 59.51, l: "B", a: 1},
{o: 59.51, f: 110.831, l: "C", a: 0},
{o: 110.831, f: 186.719, l: "E", a: 1},
{o: 186.719, f: 230.498, l: "C", a: 0},
{o: 230.498, f: 271.846, l: "B", a: 1},
{o: 271.846, f: 331.05, l: "C", a: 0}],
[{o: 0.004, f: 20.82, l: "A", a: 0},
{o: 20.82, f: 32.724, l: "A", a: 1},
{o: 32.724, f: 51.372, l: "A", a: 0},
{o: 51.372, f: 59.488, l: "A", a: 1},
{o: 59.488, f: 72.112, l: "B", a: 0},
{o: 72.112, f: 83.028, l: "A", a: 1},
{o: 83.028, f: 98.508, l: "A", a: 0},
{o: 98.508, f: 108.976, l: "A", a: 1},
{o: 108.976, f: 128.364, l: "A", a: 0},
{o: 128.364, f: 135.692, l: "A", a: 1},
{o: 135.692, f: 145.932, l: "A", a: 0},
{o: 145.932, f: 159.684, l: "A", a: 1},
{o: 159.684, f: 177.108, l: "A", a: 0},
{o: 177.108, f: 187.868, l: "A", a: 1},
{o: 187.868, f: 199.16, l: "A", a: 0},
{o: 199.16, f: 212.928, l: "A", a: 1},
{o: 212.928, f: 225.484, l: "A", a: 0},
{o: 225.484, f: 239.884, l: "A", a: 1},
{o: 239.884, f: 254.88, l: "A", a: 0},
{o: 254.88, f: 267.816, l: "A", a: 1},
{o: 267.816, f: 284.704, l: "A", a: 0},
{o: 284.704, f: 298.356, l: "A", a: 1},
{o: 298.356, f: 310.892, l: "A", a: 0}],
[{o: 0.004, f: 20.82, l: "H", a: 0},
{o: 20.82, f: 32.724, l: "G", a: 1},
{o: 32.724, f: 51.372, l: "G", a: 0},
{o: 51.372, f: 59.488, l: "G", a: 1},
{o: 59.488, f: 72.112, l: "I", a: 0},
{o: 72.112, f: 83.028, l: "F", a: 1},
{o: 83.028, f: 98.508, l: "G", a: 0},
{o: 98.508, f: 108.976, l: "F", a: 1},
{o: 108.976, f: 128.364, l: "J", a: 0},
{o: 128.364, f: 135.692, l: "K", a: 1},
{o: 135.692, f: 145.932, l: "B", a: 0},
{o: 145.932, f: 159.684, l: "B", a: 1},
{o: 159.684, f: 177.108, l: "D", a: 0},
{o: 177.108, f: 187.868, l: "G", a: 1},
{o: 187.868, f: 199.16, l: "D", a: 0},
{o: 199.16, f: 212.928, l: "G", a: 1},
{o: 212.928, f: 225.484, l: "G", a: 0},
{o: 225.484, f: 239.884, l: "E", a: 1},
{o: 239.884, f: 254.88, l: "A", a: 0},
{o: 254.88, f: 267.816, l: "A", a: 1},
{o: 267.816, f: 284.704, l: "C", a: 0},
{o: 284.704, f: 298.356, l: "E", a: 1},
{o: 298.356, f: 310.892, l: "C", a: 0}],
[{o: 0.493, f: 2.213, l: "5", a: 0},
{o: 2.213, f: 11.64, l: "4", a: 1},
{o: 11.64, f: 33.747, l: "1", a: 0},
{o: 33.747, f: 58.573, l: "3", a: 1},
{o: 58.573, f: 82.907, l: "1", a: 0},
{o: 82.907, f: 89.8, l: "3", a: 1},
{o: 89.8, f: 110.147, l: "2", a: 0},
{o: 110.147, f: 130.467, l: "1", a: 1},
{o: 130.467, f: 151.36, l: "6", a: 0},
{o: 151.36, f: 170.373, l: "1", a: 1},
{o: 170.373, f: 182.213, l: "8", a: 0},
{o: 182.213, f: 206.36, l: "4", a: 1},
{o: 206.36, f: 215, l: "1", a: 0},
{o: 215, f: 223.24, l: "2", a: 1},
{o: 223.24, f: 264.013, l: "3", a: 0},
{o: 264.013, f: 284.627, l: "2", a: 1},
{o: 284.627, f: 299.56, l: "7", a: 0},
{o: 299.56, f: 310, l: "2", a: 1},
{o: 310, f: 330.947, l: "5", a: 0}],
[{o: 0, f: 184.015, l: "a", a: 0},
{o: 184.015, f: 197.425, l: "b", a: 1},
{o: 197.425, f: 210.09, l: "b", a: 0},
{o: 210.09, f: 311.41, l: "c", a: 1},
{o: 311.41, f: 321.84, l: "d", a: 0}],
[{o: 0, f: 330.977, l: "A", a: 0},
{o: 1.997, f: 331.07, l: "A", a: 1}],
[{o: 0, f: 0.028, l: "H", a: 0},
{o: 0.028, f: 59.66, l: "G", a: 1},
{o: 59.66, f: 82.288, l: "A", a: 0},
{o: 82.288, f: 93.956, l: "C", a: 1},
{o: 93.956, f: 113.668, l: "F", a: 0},
{o: 113.668, f: 120.444, l: "G", a: 1},
{o: 120.444, f: 148.116, l: "F", a: 0},
{o: 148.116, f: 193.844, l: "G", a: 1},
{o: 193.844, f: 211.084, l: "B", a: 0},
{o: 211.084, f: 223.756, l: "F", a: 1},
{o: 223.756, f: 230.936, l: "C", a: 0},
{o: 230.936, f: 284.324, l: "G", a: 1},
{o: 284.324, f: 306.536, l: "F", a: 0},
{o: 306.536, f: 310.916, l: "A", a: 1},
{o: 310.916, f: 331.051, l: "H", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000552.ogg";
