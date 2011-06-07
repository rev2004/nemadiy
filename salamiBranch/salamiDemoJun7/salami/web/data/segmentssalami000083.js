var data = [
[{o: 0.139, f: 13.476, l: "A", a: 0},
{o: 13.476, f: 38.555, l: "B", a: 1},
{o: 38.555, f: 63.54, l: "C", a: 0},
{o: 63.54, f: 88.483, l: "B", a: 1},
{o: 88.483, f: 113.283, l: "C", a: 0},
{o: 113.283, f: 137.763, l: "B", a: 1},
{o: 137.763, f: 170.559, l: "C", a: 0}],
[{o: 0.104, f: 5.564, l: "B", a: 0},
{o: 5.564, f: 12.944, l: "A", a: 1},
{o: 12.944, f: 21.624, l: "A", a: 0},
{o: 21.624, f: 34.196, l: "A", a: 1},
{o: 34.196, f: 42.456, l: "A", a: 0},
{o: 42.456, f: 51.088, l: "A", a: 1},
{o: 51.088, f: 63.904, l: "A", a: 0},
{o: 63.904, f: 76.012, l: "A", a: 1},
{o: 76.012, f: 88.036, l: "A", a: 0},
{o: 88.036, f: 100.88, l: "A", a: 1},
{o: 100.88, f: 113.624, l: "A", a: 0},
{o: 113.624, f: 125.052, l: "A", a: 1},
{o: 125.052, f: 137.368, l: "A", a: 0},
{o: 137.368, f: 152.296, l: "A", a: 1},
{o: 152.296, f: 164.48, l: "A", a: 0}],
[{o: 0.104, f: 5.564, l: "D", a: 0},
{o: 5.564, f: 12.944, l: "E", a: 1},
{o: 12.944, f: 21.624, l: "A", a: 0},
{o: 21.624, f: 34.196, l: "A", a: 1},
{o: 34.196, f: 42.456, l: "B", a: 0},
{o: 42.456, f: 51.088, l: "B", a: 1},
{o: 51.088, f: 63.904, l: "C", a: 0},
{o: 63.904, f: 76.012, l: "A", a: 1},
{o: 76.012, f: 88.036, l: "A", a: 0},
{o: 88.036, f: 100.88, l: "C", a: 1},
{o: 100.88, f: 113.624, l: "C", a: 0},
{o: 113.624, f: 125.052, l: "F", a: 1},
{o: 125.052, f: 137.368, l: "G", a: 0},
{o: 137.368, f: 152.296, l: "C", a: 1},
{o: 152.296, f: 164.48, l: "C", a: 0}],
[{o: 0.547, f: 12.387, l: "7", a: 0},
{o: 12.387, f: 21.84, l: "2", a: 1},
{o: 21.84, f: 38.347, l: "5", a: 0},
{o: 38.347, f: 45.013, l: "1", a: 1},
{o: 45.013, f: 50.92, l: "4", a: 0},
{o: 50.92, f: 56.707, l: "1", a: 1},
{o: 56.707, f: 63.333, l: "3", a: 0},
{o: 63.333, f: 71.92, l: "2", a: 1},
{o: 71.92, f: 76.613, l: "5", a: 0},
{o: 76.613, f: 88.6, l: "2", a: 1},
{o: 88.6, f: 94.787, l: "1", a: 0},
{o: 94.787, f: 100.693, l: "4", a: 1},
{o: 100.693, f: 106.107, l: "1", a: 0},
{o: 106.107, f: 112.653, l: "3", a: 1},
{o: 112.653, f: 137.547, l: "6", a: 0},
{o: 137.547, f: 144.08, l: "1", a: 1},
{o: 144.08, f: 149.8, l: "4", a: 0},
{o: 149.8, f: 155.827, l: "1", a: 1},
{o: 155.827, f: 161.973, l: "3", a: 0},
{o: 161.973, f: 169.427, l: "8", a: 1}],
[{o: 0, f: 16.39, l: "a", a: 0},
{o: 16.39, f: 35.015, l: "b", a: 1},
{o: 35.015, f: 51.405, l: "c", a: 0},
{o: 51.405, f: 66.305, l: "c", a: 1},
{o: 66.305, f: 85.675, l: "b", a: 0},
{o: 85.675, f: 95.36, l: "c", a: 1},
{o: 95.36, f: 115.475, l: "c", a: 0},
{o: 115.475, f: 134.1, l: "d", a: 1},
{o: 134.1, f: 150.49, l: "c", a: 0},
{o: 150.49, f: 169.86, l: "c", a: 1}],
[{o: 0, f: 13.793, l: "n1", a: 0},
{o: 13.793, f: 63.936, l: "A", a: 1},
{o: 63.936, f: 113.662, l: "A", a: 0},
{o: 113.662, f: 162.598, l: "A", a: 1},
{o: 162.598, f: 170.202, l: "n2", a: 0}],
[{o: 0, f: 0.104, l: "D", a: 0},
{o: 0.104, f: 0.104, l: "B", a: 1},
{o: 0.104, f: 40.496, l: "A", a: 0},
{o: 40.496, f: 63.9, l: "B", a: 1},
{o: 63.9, f: 89.952, l: "A", a: 0},
{o: 89.952, f: 118.988, l: "B", a: 1},
{o: 118.988, f: 137.756, l: "C", a: 0},
{o: 137.756, f: 164.16, l: "B", a: 1},
{o: 164.16, f: 170.467, l: "D", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000083.ogg";
