var data = [
[{o: 0.43, f: 49.043, l: "A", a: 0},
{o: 49.043, f: 92.459, l: "A", a: 1},
{o: 92.459, f: 147.979, l: "B", a: 0},
{o: 147.979, f: 191.034, l: "A'", a: 1}],
[{o: 0.416, f: 11.028, l: "E", a: 0},
{o: 11.028, f: 28.46, l: "E", a: 1},
{o: 28.46, f: 44.316, l: "E", a: 0},
{o: 44.316, f: 57.276, l: "E", a: 1},
{o: 57.276, f: 73.448, l: "B", a: 0},
{o: 73.448, f: 88.852, l: "E", a: 1},
{o: 88.852, f: 100.616, l: "B", a: 0},
{o: 100.616, f: 110.364, l: "E", a: 1},
{o: 110.364, f: 127.324, l: "B", a: 0},
{o: 127.324, f: 145.344, l: "E", a: 1},
{o: 145.344, f: 155.608, l: "B", a: 0},
{o: 155.608, f: 171.112, l: "E", a: 1},
{o: 171.112, f: 188.176, l: "E", a: 0}],
[{o: 0.416, f: 11.028, l: "E", a: 0},
{o: 11.028, f: 28.46, l: "B", a: 1},
{o: 28.46, f: 44.316, l: "B", a: 0},
{o: 44.316, f: 57.276, l: "E", a: 1},
{o: 57.276, f: 73.448, l: "C", a: 0},
{o: 73.448, f: 88.852, l: "B", a: 1},
{o: 88.852, f: 100.616, l: "A", a: 0},
{o: 100.616, f: 110.364, l: "D", a: 1},
{o: 110.364, f: 127.324, l: "A", a: 0},
{o: 127.324, f: 145.344, l: "E", a: 1},
{o: 145.344, f: 155.608, l: "F", a: 0},
{o: 155.608, f: 171.112, l: "B", a: 1},
{o: 171.112, f: 188.176, l: "E", a: 0}],
[{o: 0.653, f: 3.56, l: "8", a: 0},
{o: 3.56, f: 8.64, l: "2", a: 1},
{o: 8.64, f: 26.933, l: "1", a: 0},
{o: 26.933, f: 32.987, l: "2", a: 1},
{o: 32.987, f: 41.88, l: "1", a: 0},
{o: 41.88, f: 49.493, l: "2", a: 1},
{o: 49.493, f: 58.187, l: "1", a: 0},
{o: 58.187, f: 64.093, l: "2", a: 1},
{o: 64.093, f: 86.467, l: "1", a: 0},
{o: 86.467, f: 92.28, l: "5", a: 1},
{o: 92.28, f: 99.6, l: "3", a: 0},
{o: 99.6, f: 113.44, l: "4", a: 1},
{o: 113.44, f: 120.44, l: "1", a: 0},
{o: 120.44, f: 127.68, l: "3", a: 1},
{o: 127.68, f: 134.773, l: "4", a: 0},
{o: 134.773, f: 146.84, l: "3", a: 1},
{o: 146.84, f: 154.92, l: "1", a: 0},
{o: 154.92, f: 161.187, l: "2", a: 1},
{o: 161.187, f: 168.68, l: "1", a: 0},
{o: 168.68, f: 174.907, l: "2", a: 1},
{o: 174.907, f: 181.893, l: "1", a: 0},
{o: 181.893, f: 186.933, l: "6", a: 1},
{o: 186.933, f: 190.92, l: "7", a: 0}],
[{o: 0, f: 52.895, l: "a", a: 0},
{o: 52.895, f: 91.635, l: "b", a: 1},
{o: 91.635, f: 112.495, l: "c", a: 0},
{o: 112.495, f: 140.06, l: "c", a: 1},
{o: 140.06, f: 151.98, l: "d", a: 0},
{o: 151.98, f: 190.72, l: "b", a: 1}],
[{o: 0, f: 190.682, l: "A", a: 0},
{o: 0.418, f: 191.007, l: "A", a: 1}],
[{o: 0, f: 0.344, l: "J", a: 0},
{o: 0.344, f: 11.392, l: "B", a: 1},
{o: 11.392, f: 99.764, l: "A", a: 0},
{o: 99.764, f: 113.492, l: "I", a: 1},
{o: 113.492, f: 135.744, l: "A", a: 0},
{o: 135.744, f: 159.816, l: "I", a: 1},
{o: 159.816, f: 188.06, l: "A", a: 0},
{o: 188.06, f: 188.588, l: "C", a: 1},
{o: 188.588, f: 191.034, l: "J", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000431.ogg";
