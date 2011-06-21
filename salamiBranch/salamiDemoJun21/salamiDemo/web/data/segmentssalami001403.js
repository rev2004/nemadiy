var data = [
[{o: 0.046, f: 10.337, l: "Z", a: 0},
{o: 10.337, f: 41.705, l: "A", a: 1},
{o: 41.705, f: 80.107, l: "A", a: 0},
{o: 80.107, f: 111.33, l: "B", a: 1},
{o: 111.33, f: 132.795, l: "C", a: 0},
{o: 132.795, f: 157.946, l: "B", a: 1},
{o: 157.946, f: 178.623, l: "C", a: 0},
{o: 178.623, f: 208.567, l: "A", a: 1},
{o: 208.567, f: 239.58, l: "A", a: 0},
{o: 239.58, f: 339.267, l: "D", a: 1},
{o: 339.267, f: 360.803, l: "C", a: 0},
{o: 360.803, f: 369.006, l: "Z", a: 1}],
[{o: 0.008, f: 13.904, l: "B", a: 0},
{o: 13.904, f: 38.22, l: "A", a: 1},
{o: 38.22, f: 67.82, l: "A", a: 0},
{o: 67.82, f: 105.872, l: "C", a: 1},
{o: 105.872, f: 128.796, l: "A", a: 0},
{o: 128.796, f: 154.068, l: "C", a: 1},
{o: 154.068, f: 174.248, l: "A", a: 0},
{o: 174.248, f: 203.108, l: "D", a: 1},
{o: 203.108, f: 235.876, l: "D", a: 0},
{o: 235.876, f: 256.156, l: "D", a: 1},
{o: 256.156, f: 287.312, l: "E", a: 0},
{o: 287.312, f: 305.824, l: "F", a: 1},
{o: 305.824, f: 337.072, l: "G", a: 0},
{o: 337.072, f: 367.424, l: "C", a: 1}],
[{o: 0.008, f: 13.904, l: "E", a: 0},
{o: 13.904, f: 38.22, l: "A", a: 1},
{o: 38.22, f: 67.82, l: "A", a: 0},
{o: 67.82, f: 105.872, l: "C", a: 1},
{o: 105.872, f: 128.796, l: "B", a: 0},
{o: 128.796, f: 154.068, l: "C", a: 1},
{o: 154.068, f: 174.248, l: "B", a: 0},
{o: 174.248, f: 203.108, l: "F", a: 1},
{o: 203.108, f: 235.876, l: "D", a: 0},
{o: 235.876, f: 256.156, l: "D", a: 1},
{o: 256.156, f: 287.312, l: "G", a: 0},
{o: 287.312, f: 305.824, l: "H", a: 1},
{o: 305.824, f: 337.072, l: "I", a: 0},
{o: 337.072, f: 367.424, l: "J", a: 1}],
[{o: 0.547, f: 4.587, l: "7", a: 0},
{o: 4.587, f: 9.493, l: "6", a: 1},
{o: 9.493, f: 35.707, l: "1", a: 0},
{o: 35.707, f: 41.947, l: "2", a: 1},
{o: 41.947, f: 66.88, l: "1", a: 0},
{o: 66.88, f: 78.72, l: "2", a: 1},
{o: 78.72, f: 151.613, l: "1", a: 0},
{o: 151.613, f: 156.693, l: "5", a: 1},
{o: 156.693, f: 202.973, l: "1", a: 0},
{o: 202.973, f: 209.067, l: "2", a: 1},
{o: 209.067, f: 233.373, l: "1", a: 0},
{o: 233.373, f: 238.56, l: "2", a: 1},
{o: 238.56, f: 251.787, l: "1", a: 0},
{o: 251.787, f: 257.773, l: "4", a: 1},
{o: 257.773, f: 338.52, l: "3", a: 0},
{o: 338.52, f: 359.613, l: "1", a: 1},
{o: 359.613, f: 365.32, l: "2", a: 0},
{o: 365.32, f: 368.813, l: "8", a: 1}],
[{o: 0, f: 85.675, l: "a", a: 0},
{o: 85.675, f: 177.31, l: "b", a: 1},
{o: 177.31, f: 251.81, l: "a", a: 0},
{o: 251.81, f: 307.685, l: "c", a: 1},
{o: 307.685, f: 368.775, l: "a", a: 0}],
[{o: 0, f: 10.658, l: "n1", a: 0},
{o: 10.658, f: 41.703, l: "A", a: 1},
{o: 41.703, f: 68.476, l: "A", a: 0},
{o: 68.476, f: 81.978, l: "n3", a: 1},
{o: 81.978, f: 92.183, l: "B", a: 0},
{o: 92.183, f: 102.655, l: "C", a: 1},
{o: 102.655, f: 114.114, l: "C", a: 0},
{o: 114.114, f: 124.331, l: "B", a: 1},
{o: 124.331, f: 134.618, l: "B", a: 0},
{o: 134.618, f: 144.927, l: "C", a: 1},
{o: 144.927, f: 154.215, l: "C", a: 0},
{o: 154.215, f: 159.579, l: "n7", a: 1},
{o: 159.579, f: 169.924, l: "B", a: 0},
{o: 169.924, f: 178.666, l: "n8", a: 1},
{o: 178.666, f: 208.887, l: "A", a: 0},
{o: 208.887, f: 236.414, l: "A", a: 1},
{o: 236.414, f: 346.999, l: "n10", a: 0},
{o: 346.999, f: 357.251, l: "B", a: 1},
{o: 357.251, f: 368.594, l: "n11", a: 0}],
[{o: 0, f: 0.004, l: "G", a: 0},
{o: 0.004, f: 7.2, l: "C", a: 1},
{o: 7.2, f: 118.116, l: "A", a: 0},
{o: 118.116, f: 131.012, l: "E", a: 1},
{o: 131.012, f: 147.348, l: "A", a: 0},
{o: 147.348, f: 158.648, l: "F", a: 1},
{o: 158.648, f: 176.616, l: "E", a: 0},
{o: 176.616, f: 367.176, l: "A", a: 1},
{o: 367.176, f: 368.919, l: "G", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami001403.ogg";

var artist = "Snuckafoo";

var track = "Bucket of Ham";
