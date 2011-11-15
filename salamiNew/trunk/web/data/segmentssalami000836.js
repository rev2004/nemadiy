var data = [
[{o: 0.291, f: 20.655, l: "A", a: 0},
{o: 20.655, f: 60.397, l: "A", a: 1},
{o: 60.397, f: 122.659, l: "B", a: 0},
{o: 122.659, f: 173.832, l: "C", a: 1},
{o: 173.832, f: 193.77, l: "A", a: 0},
{o: 193.77, f: 235.3, l: "A", a: 1},
{o: 235.3, f: 277.349, l: "B", a: 0},
{o: 277.349, f: 329.15, l: "C", a: 1},
{o: 329.15, f: 349.078, l: "A", a: 0},
{o: 349.078, f: 438.196, l: "A", a: 1}],
[{o: 0, f: 438.179, l: "A", a: 0}],
[{o: 0, f: 438.179, l: "A", a: 0}],
[{o: 0.573, f: 45.093, l: "1", a: 0},
{o: 45.093, f: 121.6, l: "2", a: 1},
{o: 121.6, f: 161.16, l: "3", a: 0},
{o: 161.16, f: 172.187, l: "6", a: 1},
{o: 172.187, f: 180.36, l: "1", a: 0},
{o: 180.36, f: 195.133, l: "7", a: 1},
{o: 195.133, f: 219.547, l: "1", a: 0},
{o: 219.547, f: 234.227, l: "2", a: 1},
{o: 234.227, f: 273.053, l: "4", a: 0},
{o: 273.053, f: 316.213, l: "3", a: 1},
{o: 316.213, f: 326.813, l: "6", a: 0},
{o: 326.813, f: 333.067, l: "1", a: 1},
{o: 333.067, f: 348.52, l: "2", a: 0},
{o: 348.52, f: 390.76, l: "5", a: 1},
{o: 390.76, f: 432.24, l: "4", a: 0},
{o: 432.24, f: 436.627, l: "8", a: 1}],
[{o: 0, f: 105.045, l: "a", a: 0},
{o: 105.045, f: 172.84, l: "b", a: 1},
{o: 172.84, f: 278.63, l: "a", a: 0},
{o: 278.63, f: 330.78, l: "c", a: 1},
{o: 330.78, f: 381.44, l: "d", a: 0},
{o: 381.44, f: 438.06, l: "d", a: 1}],
[{o: 0, f: 1.602, l: "n1", a: 0},
{o: 1.602, f: 18.065, l: "A", a: 1},
{o: 18.065, f: 51.409, l: "n2", a: 0},
{o: 51.409, f: 68.545, l: "A", a: 1},
{o: 68.545, f: 93.205, l: "B", a: 0},
{o: 93.205, f: 102.655, l: "n3", a: 1},
{o: 102.655, f: 124.9, l: "A", a: 0},
{o: 124.9, f: 140.434, l: "C", a: 1},
{o: 140.434, f: 145.682, l: "n5", a: 0},
{o: 145.682, f: 161.414, l: "C", a: 1},
{o: 161.414, f: 173.511, l: "n6", a: 0},
{o: 173.511, f: 191.263, l: "A", a: 1},
{o: 191.263, f: 236.693, l: "n7", a: 0},
{o: 236.693, f: 257.138, l: "A", a: 1},
{o: 257.138, f: 279.673, l: "A", a: 0},
{o: 279.673, f: 295.323, l: "C", a: 1},
{o: 295.323, f: 357.541, l: "n10", a: 0},
{o: 357.541, f: 382.177, l: "B", a: 1},
{o: 382.177, f: 389.224, l: "n11", a: 0},
{o: 389.224, f: 413.791, l: "B", a: 1},
{o: 413.791, f: 438.068, l: "n12", a: 0}],
[{o: 0, f: 0.232, l: "H", a: 0},
{o: 0.232, f: 1.756, l: "G", a: 1},
{o: 1.756, f: 319.728, l: "A", a: 0},
{o: 319.728, f: 328.988, l: "G", a: 1},
{o: 328.988, f: 344.404, l: "A", a: 0},
{o: 344.404, f: 403.68, l: "F", a: 1},
{o: 403.68, f: 412.004, l: "A", a: 0},
{o: 412.004, f: 426.604, l: "F", a: 1},
{o: 426.604, f: 433.448, l: "A", a: 0},
{o: 433.448, f: 438.179, l: "H", a: 1}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000836.ogg";

var artist = "Ani DiFranco";

var track = "Providence";