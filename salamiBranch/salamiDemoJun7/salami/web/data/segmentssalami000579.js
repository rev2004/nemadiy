var data = [
[{o: 0.454, f: 8.51, l: "A", a: 0},
{o: 8.51, f: 20.94, l: "B", a: 1},
{o: 20.94, f: 33.203, l: "B", a: 0},
{o: 33.203, f: 53.551, l: "C", a: 1},
{o: 53.551, f: 65.7, l: "B", a: 0},
{o: 65.7, f: 85.945, l: "C", a: 1},
{o: 85.945, f: 98.105, l: "B", a: 0},
{o: 98.105, f: 118.376, l: "C", a: 1},
{o: 118.376, f: 131.251, l: "B", a: 0},
{o: 131.251, f: 139.82, l: "A", a: 1},
{o: 139.82, f: 162.424, l: "B", a: 0}],
[{o: 0.152, f: 8.508, l: "C", a: 0},
{o: 8.508, f: 31.656, l: "A", a: 1},
{o: 31.656, f: 43.352, l: "A", a: 0},
{o: 43.352, f: 64.164, l: "A", a: 1},
{o: 64.164, f: 86.428, l: "B", a: 0},
{o: 86.428, f: 97.528, l: "A", a: 1},
{o: 97.528, f: 118.336, l: "B", a: 0},
{o: 118.336, f: 139.884, l: "D", a: 1},
{o: 139.884, f: 160.536, l: "E", a: 0}],
[{o: 0.152, f: 8.508, l: "C", a: 0},
{o: 8.508, f: 31.656, l: "A", a: 1},
{o: 31.656, f: 43.352, l: "A", a: 0},
{o: 43.352, f: 64.164, l: "A", a: 1},
{o: 64.164, f: 86.428, l: "B", a: 0},
{o: 86.428, f: 97.528, l: "D", a: 1},
{o: 97.528, f: 118.336, l: "B", a: 0},
{o: 118.336, f: 139.884, l: "E", a: 1},
{o: 139.884, f: 160.536, l: "F", a: 0}],
[{o: 0.413, f: 8.8, l: "6", a: 0},
{o: 8.8, f: 31.4, l: "2", a: 1},
{o: 31.4, f: 37, l: "8", a: 0},
{o: 37, f: 43.573, l: "3", a: 1},
{o: 43.573, f: 54.773, l: "1", a: 0},
{o: 54.773, f: 65.4, l: "2", a: 1},
{o: 65.4, f: 76.533, l: "3", a: 0},
{o: 76.533, f: 90.16, l: "1", a: 1},
{o: 90.16, f: 97.76, l: "7", a: 0},
{o: 97.76, f: 108.4, l: "3", a: 1},
{o: 108.4, f: 119.2, l: "1", a: 0},
{o: 119.2, f: 130.493, l: "2", a: 1},
{o: 130.493, f: 140.76, l: "1", a: 0},
{o: 140.76, f: 153.347, l: "4", a: 1},
{o: 153.347, f: 162.227, l: "5", a: 0}],
[{o: 0, f: 18.625, l: "a", a: 0},
{o: 18.625, f: 36.505, l: "b", a: 1},
{o: 36.505, f: 48.425, l: "c", a: 0},
{o: 48.425, f: 64.815, l: "a", a: 1},
{o: 64.815, f: 76.735, l: "c", a: 0},
{o: 76.735, f: 93.87, l: "a", a: 1},
{o: 93.87, f: 101.32, l: "d", a: 0},
{o: 101.32, f: 113.24, l: "c", a: 1},
{o: 113.24, f: 130.375, l: "a", a: 0},
{o: 130.375, f: 161.665, l: "e", a: 1}],
[{o: 0, f: 0.441, l: "n1", a: 0},
{o: 0.441, f: 20.944, l: "A", a: 1},
{o: 20.944, f: 45.418, l: "n2", a: 0},
{o: 45.418, f: 65.736, l: "A", a: 1},
{o: 65.736, f: 77.845, l: "n3", a: 0},
{o: 77.845, f: 98.081, l: "A", a: 1},
{o: 98.081, f: 110.225, l: "n4", a: 0},
{o: 110.225, f: 131.286, l: "A", a: 1},
{o: 131.286, f: 153.043, l: "A", a: 0},
{o: 153.043, f: 162.4, l: "n5", a: 1}],
[{o: 0, f: 0.152, l: "F", a: 0},
{o: 0.152, f: 0.152, l: "B", a: 1},
{o: 0.152, f: 44.368, l: "A", a: 0},
{o: 44.368, f: 76.792, l: "D", a: 1},
{o: 76.792, f: 109.184, l: "C", a: 0},
{o: 109.184, f: 132.332, l: "D", a: 1},
{o: 132.332, f: 160.036, l: "E", a: 0},
{o: 160.036, f: 160.536, l: "D", a: 1},
{o: 160.536, f: 162.373, l: "F", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000579.ogg";
