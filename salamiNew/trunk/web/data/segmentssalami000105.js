var data = [
[{o: 0.299, f: 103.935, l: "N", a: 0},
{o: 103.935, f: 225.234, l: "A", a: 1},
{o: 225.234, f: 252.207, l: "B", a: 0},
{o: 252.207, f: 296.948, l: "B'", a: 1},
{o: 296.948, f: 349.42, l: "B''", a: 0},
{o: 349.42, f: 415.242, l: "C", a: 1},
{o: 415.242, f: 448.022, l: "B", a: 0},
{o: 448.022, f: 501.993, l: "B'", a: 1},
{o: 501.993, f: 587.096, l: "A'", a: 0}],
[{o: 0, f: 587.077, l: "A", a: 0}],
[{o: 0, f: 587.077, l: "A", a: 0}],
[{o: 0.493, f: 3.76, l: "4", a: 0},
{o: 3.76, f: 9.867, l: "6", a: 1},
{o: 9.867, f: 14.773, l: "4", a: 0},
{o: 14.773, f: 20.373, l: "6", a: 1},
{o: 20.373, f: 37.067, l: "4", a: 0},
{o: 37.067, f: 43.093, l: "7", a: 1},
{o: 43.093, f: 55.48, l: "2", a: 0},
{o: 55.48, f: 73.627, l: "4", a: 1},
{o: 73.627, f: 85.12, l: "2", a: 0},
{o: 85.12, f: 89.987, l: "6", a: 1},
{o: 89.987, f: 141.36, l: "2", a: 0},
{o: 141.36, f: 216.107, l: "1", a: 1},
{o: 216.107, f: 224.507, l: "5", a: 0},
{o: 224.507, f: 267.893, l: "3", a: 1},
{o: 267.893, f: 273.24, l: "8", a: 0},
{o: 273.24, f: 279.427, l: "3", a: 1},
{o: 279.427, f: 293.84, l: "1", a: 0},
{o: 293.84, f: 345.64, l: "3", a: 1},
{o: 345.64, f: 356.56, l: "4", a: 0},
{o: 356.56, f: 404.933, l: "1", a: 1},
{o: 404.933, f: 414.107, l: "5", a: 0},
{o: 414.107, f: 438.027, l: "3", a: 1},
{o: 438.027, f: 493.067, l: "2", a: 0},
{o: 493.067, f: 501.573, l: "5", a: 1},
{o: 501.573, f: 524.067, l: "2", a: 0},
{o: 524.067, f: 580.4, l: "1", a: 1},
{o: 580.4, f: 584.333, l: "5", a: 0}],
[{o: 0, f: 114.73, l: "a", a: 0},
{o: 114.73, f: 155.705, l: "b", a: 1},
{o: 155.705, f: 216.795, l: "b", a: 0},
{o: 216.795, f: 511.07, l: "c", a: 1},
{o: 511.07, f: 587.805, l: "b", a: 0}],
[{o: 0, f: 112.559, l: "n1", a: 0},
{o: 112.559, f: 127.466, l: "E", a: 1},
{o: 127.466, f: 156.851, l: "n2", a: 0},
{o: 156.851, f: 171.549, l: "A", a: 1},
{o: 171.549, f: 186.27, l: "A", a: 0},
{o: 186.27, f: 201.015, l: "C", a: 1},
{o: 201.015, f: 216.352, l: "A", a: 0},
{o: 216.352, f: 225.652, l: "n3", a: 1},
{o: 225.652, f: 237.621, l: "B", a: 0},
{o: 237.621, f: 297.494, l: "n4", a: 1},
{o: 297.494, f: 307.885, l: "B", a: 0},
{o: 307.885, f: 324.569, l: "n5", a: 1},
{o: 324.569, f: 336.271, l: "B", a: 0},
{o: 336.271, f: 349.402, l: "n6", a: 1},
{o: 349.402, f: 363.16, l: "D", a: 0},
{o: 363.16, f: 376.999, l: "D", a: 1},
{o: 376.999, f: 510.073, l: "n7", a: 0},
{o: 510.073, f: 525.003, l: "E", a: 1},
{o: 525.003, f: 539.701, l: "A", a: 0},
{o: 539.701, f: 554.214, l: "C", a: 1},
{o: 554.214, f: 569.028, l: "A", a: 0},
{o: 569.028, f: 587.047, l: "n8", a: 1}],
[{o: 0, f: 0.112, l: "I", a: 0},
{o: 0.112, f: 13.72, l: "H", a: 1},
{o: 13.72, f: 30.756, l: "F", a: 0},
{o: 30.756, f: 36.864, l: "B", a: 1},
{o: 36.864, f: 60.828, l: "C", a: 0},
{o: 60.828, f: 89.26, l: "F", a: 1},
{o: 89.26, f: 112.856, l: "H", a: 0},
{o: 112.856, f: 128.4, l: "E", a: 1},
{o: 128.4, f: 135.264, l: "H", a: 0},
{o: 135.264, f: 141.516, l: "E", a: 1},
{o: 141.516, f: 156.276, l: "F", a: 0},
{o: 156.276, f: 213.996, l: "E", a: 1},
{o: 213.996, f: 248.592, l: "F", a: 0},
{o: 248.592, f: 311.456, l: "B", a: 1},
{o: 311.456, f: 391.096, l: "F", a: 0},
{o: 391.096, f: 430.824, l: "B", a: 1},
{o: 430.824, f: 499.936, l: "F", a: 0},
{o: 499.936, f: 539.62, l: "E", a: 1},
{o: 539.62, f: 545.16, l: "F", a: 0},
{o: 545.16, f: 553.788, l: "C", a: 1},
{o: 553.788, f: 571.688, l: "E", a: 0},
{o: 571.688, f: 582.536, l: "C", a: 1},
{o: 582.536, f: 587.012, l: "B", a: 0},
{o: 587.012, f: 587.077, l: "I", a: 1}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000105.ogg";

var artist = "Ensemble Ro mulo Larrea Vero nica Larc";

var track = "Contrabajisimo";
