var data = [
[{o: 0.049, f: 8.947, l: "I", a: 0},
{o: 8.947, f: 32.665, l: "V", a: 1},
{o: 32.665, f: 56.185, l: "V", a: 0},
{o: 56.185, f: 79.439, l: "V", a: 1},
{o: 79.439, f: 102.483, l: "V", a: 0},
{o: 102.483, f: 132.788, l: "V", a: 1}],
[{o: 0.048, f: 17.868, l: "B", a: 0},
{o: 17.868, f: 30.256, l: "B", a: 1},
{o: 30.256, f: 38.584, l: "B", a: 0},
{o: 38.584, f: 55.64, l: "B", a: 1},
{o: 55.64, f: 62.864, l: "A", a: 0},
{o: 62.864, f: 79.356, l: "B", a: 1},
{o: 79.356, f: 95.344, l: "B", a: 0},
{o: 95.344, f: 110.912, l: "B", a: 1},
{o: 110.912, f: 121.052, l: "C", a: 0},
{o: 121.052, f: 128.552, l: "D", a: 1}],
[{o: 0.048, f: 17.868, l: "B", a: 0},
{o: 17.868, f: 30.256, l: "A", a: 1},
{o: 30.256, f: 38.584, l: "A", a: 0},
{o: 38.584, f: 55.64, l: "A", a: 1},
{o: 55.64, f: 62.864, l: "C", a: 0},
{o: 62.864, f: 79.356, l: "D", a: 1},
{o: 79.356, f: 95.344, l: "A", a: 0},
{o: 95.344, f: 110.912, l: "A", a: 1},
{o: 110.912, f: 121.052, l: "E", a: 0},
{o: 121.052, f: 128.552, l: "F", a: 1}],
[{o: 0.36, f: 7.013, l: "8", a: 0},
{o: 7.013, f: 15.667, l: "1", a: 1},
{o: 15.667, f: 22.173, l: "2", a: 0},
{o: 22.173, f: 27.107, l: "1", a: 1},
{o: 27.107, f: 31.947, l: "3", a: 0},
{o: 31.947, f: 39.36, l: "1", a: 1},
{o: 39.36, f: 53.907, l: "2", a: 0},
{o: 53.907, f: 62.693, l: "6", a: 1},
{o: 62.693, f: 70.053, l: "7", a: 0},
{o: 70.053, f: 78.68, l: "3", a: 1},
{o: 78.68, f: 86.44, l: "1", a: 0},
{o: 86.44, f: 92.213, l: "2", a: 1},
{o: 92.213, f: 101.707, l: "4", a: 0},
{o: 101.707, f: 109.453, l: "1", a: 1},
{o: 109.453, f: 122.467, l: "2", a: 0},
{o: 122.467, f: 131.533, l: "5", a: 1}],
[{o: 0, f: 23.84, l: "a", a: 0},
{o: 23.84, f: 34.27, l: "b", a: 1},
{o: 34.27, f: 40.975, l: "c", a: 0},
{o: 40.975, f: 93.87, l: "d", a: 1},
{o: 93.87, f: 110.26, l: "b", a: 0},
{o: 110.26, f: 116.965, l: "e", a: 1},
{o: 116.965, f: 130.375, l: "b", a: 0},
{o: 130.375, f: 131.865, l: "f", a: 1}],
[{o: 0, f: 63.031, l: "n1", a: 0},
{o: 63.031, f: 78.495, l: "A", a: 1},
{o: 78.495, f: 86.251, l: "n2", a: 0},
{o: 86.251, f: 101.39, l: "A", a: 1},
{o: 101.39, f: 132.679, l: "n3", a: 0}],
[{o: 0, f: 0.048, l: "F", a: 0},
{o: 0.048, f: 29.784, l: "C", a: 1},
{o: 29.784, f: 101.004, l: "B", a: 0},
{o: 101.004, f: 128.552, l: "E", a: 1},
{o: 128.552, f: 132.782, l: "F", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000540.ogg";
