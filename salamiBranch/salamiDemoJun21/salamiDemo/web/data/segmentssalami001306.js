var data = [
[{o: 0.07, f: 0.975, l: "Y", a: 0},
{o: 0.975, f: 19.346, l: "A", a: 1},
{o: 19.346, f: 60.901, l: "C", a: 0},
{o: 60.901, f: 84.035, l: "F", a: 1},
{o: 84.035, f: 120.93, l: "J", a: 0},
{o: 120.93, f: 139.392, l: "A", a: 1},
{o: 139.392, f: 180.933, l: "C", a: 0},
{o: 180.933, f: 203.972, l: "F", a: 1},
{o: 203.972, f: 240.992, l: "J", a: 0},
{o: 240.992, f: 263.982, l: "N", a: 1},
{o: 263.982, f: 287.013, l: "F", a: 0},
{o: 287.013, f: 324.071, l: "J", a: 1},
{o: 324.071, f: 367.404, l: "A", a: 0}],
[{o: 0.008, f: 14.724, l: "A", a: 0},
{o: 14.724, f: 33.48, l: "C", a: 1},
{o: 33.48, f: 55.972, l: "C", a: 0},
{o: 55.972, f: 78.468, l: "C", a: 1},
{o: 78.468, f: 87.12, l: "B", a: 0},
{o: 87.12, f: 106.736, l: "C", a: 1},
{o: 106.736, f: 122.332, l: "C", a: 0},
{o: 122.332, f: 139.916, l: "C", a: 1},
{o: 139.916, f: 151.732, l: "D", a: 0},
{o: 151.732, f: 171.356, l: "C", a: 1},
{o: 171.356, f: 190.076, l: "C", a: 0},
{o: 190.076, f: 200.492, l: "B", a: 1},
{o: 200.492, f: 223.852, l: "C", a: 0},
{o: 223.852, f: 242.584, l: "C", a: 1},
{o: 242.584, f: 256.164, l: "E", a: 0},
{o: 256.164, f: 265.672, l: "F", a: 1},
{o: 265.672, f: 283.272, l: "G", a: 0},
{o: 283.272, f: 296.54, l: "C", a: 1},
{o: 296.54, f: 309.816, l: "H", a: 0},
{o: 309.816, f: 325.412, l: "C", a: 1},
{o: 325.412, f: 345.556, l: "I", a: 0},
{o: 345.556, f: 356.904, l: "J", a: 1}],
[{o: 0.008, f: 14.724, l: "E", a: 0},
{o: 14.724, f: 33.48, l: "C", a: 1},
{o: 33.48, f: 55.972, l: "C", a: 0},
{o: 55.972, f: 78.468, l: "D", a: 1},
{o: 78.468, f: 87.12, l: "F", a: 0},
{o: 87.12, f: 106.736, l: "B", a: 1},
{o: 106.736, f: 122.332, l: "A", a: 0},
{o: 122.332, f: 139.916, l: "G", a: 1},
{o: 139.916, f: 151.732, l: "H", a: 0},
{o: 151.732, f: 171.356, l: "C", a: 1},
{o: 171.356, f: 190.076, l: "D", a: 0},
{o: 190.076, f: 200.492, l: "I", a: 1},
{o: 200.492, f: 223.852, l: "B", a: 0},
{o: 223.852, f: 242.584, l: "A", a: 1},
{o: 242.584, f: 256.164, l: "J", a: 0},
{o: 256.164, f: 265.672, l: "K", a: 1},
{o: 265.672, f: 283.272, l: "L", a: 0},
{o: 283.272, f: 296.54, l: "B", a: 1},
{o: 296.54, f: 309.816, l: "M", a: 0},
{o: 309.816, f: 325.412, l: "A", a: 1},
{o: 325.412, f: 345.556, l: "N", a: 0},
{o: 345.556, f: 356.904, l: "O", a: 1}],
[{o: 0.867, f: 6.92, l: "3", a: 0},
{o: 6.92, f: 13.28, l: "5", a: 1},
{o: 13.28, f: 24.827, l: "4", a: 0},
{o: 24.827, f: 34.067, l: "2", a: 1},
{o: 34.067, f: 43.293, l: "3", a: 0},
{o: 43.293, f: 64.053, l: "2", a: 1},
{o: 64.053, f: 120.587, l: "1", a: 0},
{o: 120.587, f: 133.853, l: "5", a: 1},
{o: 133.853, f: 144.253, l: "4", a: 0},
{o: 144.253, f: 156.36, l: "2", a: 1},
{o: 156.36, f: 165.013, l: "3", a: 0},
{o: 165.013, f: 183.48, l: "2", a: 1},
{o: 183.48, f: 240.013, l: "1", a: 0},
{o: 240.013, f: 260.733, l: "6", a: 1},
{o: 260.733, f: 322.52, l: "1", a: 0},
{o: 322.52, f: 330.013, l: "3", a: 1},
{o: 330.013, f: 336.933, l: "5", a: 0},
{o: 336.933, f: 350.187, l: "4", a: 1},
{o: 350.187, f: 358.8, l: "8", a: 0},
{o: 358.8, f: 367.24, l: "7", a: 1}],
[{o: 0, f: 20.86, l: "a", a: 0},
{o: 20.86, f: 54.385, l: "b", a: 1},
{o: 54.385, f: 122.925, l: "c", a: 0},
{o: 122.925, f: 143.785, l: "a", a: 1},
{o: 143.785, f: 173.585, l: "b", a: 0},
{o: 173.585, f: 242.87, l: "c", a: 1},
{o: 242.87, f: 259.26, l: "d", a: 0},
{o: 259.26, f: 326.31, l: "c", a: 1},
{o: 326.31, f: 344.935, l: "a", a: 0},
{o: 344.935, f: 367.285, l: "e", a: 1}],
[{o: 0, f: 0.917, l: "n1", a: 0},
{o: 0.917, f: 10.124, l: "B", a: 1},
{o: 10.124, f: 19.331, l: "D", a: 0},
{o: 19.331, f: 28.595, l: "B", a: 1},
{o: 28.595, f: 37.802, l: "F", a: 0},
{o: 37.802, f: 47.067, l: "B", a: 1},
{o: 47.067, f: 59.443, l: "E", a: 0},
{o: 59.443, f: 82.524, l: "C", a: 1},
{o: 82.524, f: 100.984, l: "A", a: 0},
{o: 100.984, f: 120.895, l: "A", a: 1},
{o: 120.895, f: 130.125, l: "B", a: 0},
{o: 130.125, f: 139.355, l: "D", a: 1},
{o: 139.355, f: 148.608, l: "B", a: 0},
{o: 148.608, f: 157.826, l: "F", a: 1},
{o: 157.826, f: 167.056, l: "B", a: 0},
{o: 167.056, f: 179.455, l: "E", a: 1},
{o: 179.455, f: 202.524, l: "C", a: 0},
{o: 202.524, f: 220.973, l: "A", a: 1},
{o: 220.973, f: 239.467, l: "A", a: 0},
{o: 239.467, f: 262.49, l: "n5", a: 1},
{o: 262.49, f: 285.571, l: "C", a: 0},
{o: 285.571, f: 304.065, l: "A", a: 1},
{o: 304.065, f: 323.976, l: "A", a: 0},
{o: 323.976, f: 333.206, l: "B", a: 1},
{o: 333.206, f: 343.562, l: "D", a: 0},
{o: 343.562, f: 353.919, l: "G", a: 1},
{o: 353.919, f: 367.107, l: "G", a: 0}],
[{o: 0, f: 0.008, l: "H", a: 0},
{o: 0.008, f: 51.652, l: "G", a: 1},
{o: 51.652, f: 78.468, l: "E", a: 0},
{o: 78.468, f: 120.004, l: "A", a: 1},
{o: 120.004, f: 171.632, l: "G", a: 0},
{o: 171.632, f: 197.876, l: "E", a: 1},
{o: 197.876, f: 231.36, l: "A", a: 0},
{o: 231.36, f: 263.028, l: "C", a: 1},
{o: 263.028, f: 281.536, l: "E", a: 0},
{o: 281.536, f: 323.076, l: "A", a: 1},
{o: 323.076, f: 333.172, l: "G", a: 0},
{o: 333.172, f: 356.904, l: "C", a: 1},
{o: 356.904, f: 367.36, l: "H", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami001306.ogg";

var artist = "RWC MDB P 2001 M04";

var track = "3";
