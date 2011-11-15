var data = [
[{o: 4.493, f: 16.524, l: "A'", a: 0},
{o: 16.524, f: 28.571, l: "A", a: 1},
{o: 28.571, f: 44.546, l: "B", a: 0},
{o: 44.546, f: 52.563, l: "C", a: 1},
{o: 52.563, f: 68.523, l: "D", a: 0},
{o: 68.523, f: 119.528, l: "E", a: 1},
{o: 119.528, f: 148.261, l: "F", a: 0},
{o: 148.261, f: 161.128, l: "G", a: 1}],
[{o: 0.016, f: 11.036, l: "A", a: 0},
{o: 11.036, f: 22.848, l: "D", a: 1},
{o: 22.848, f: 36.988, l: "D", a: 0},
{o: 36.988, f: 51.208, l: "D", a: 1},
{o: 51.208, f: 65.468, l: "D", a: 0},
{o: 65.468, f: 75.972, l: "D", a: 1},
{o: 75.972, f: 87.976, l: "D", a: 0},
{o: 87.976, f: 102.756, l: "D", a: 1},
{o: 102.756, f: 119.472, l: "D", a: 0},
{o: 119.472, f: 128.476, l: "D", a: 1},
{o: 128.476, f: 147.52, l: "D", a: 0},
{o: 147.52, f: 160.72, l: "D", a: 1}],
[{o: 0.016, f: 11.036, l: "E", a: 0},
{o: 11.036, f: 22.848, l: "D", a: 1},
{o: 22.848, f: 36.988, l: "D", a: 0},
{o: 36.988, f: 51.208, l: "D", a: 1},
{o: 51.208, f: 65.468, l: "D", a: 0},
{o: 65.468, f: 75.972, l: "C", a: 1},
{o: 75.972, f: 87.976, l: "C", a: 0},
{o: 87.976, f: 102.756, l: "B", a: 1},
{o: 102.756, f: 119.472, l: "B", a: 0},
{o: 119.472, f: 128.476, l: "A", a: 1},
{o: 128.476, f: 147.52, l: "A", a: 0},
{o: 147.52, f: 160.72, l: "F", a: 1}],
[{o: 0.44, f: 5.227, l: "7", a: 0},
{o: 5.227, f: 16.253, l: "5", a: 1},
{o: 16.253, f: 28.987, l: "2", a: 0},
{o: 28.987, f: 43.973, l: "1", a: 1},
{o: 43.973, f: 52.973, l: "6", a: 0},
{o: 52.973, f: 68.747, l: "1", a: 1},
{o: 68.747, f: 92.96, l: "4", a: 0},
{o: 92.96, f: 119.827, l: "3", a: 1},
{o: 119.827, f: 148.107, l: "1", a: 0},
{o: 148.107, f: 157.147, l: "2", a: 1},
{o: 157.147, f: 160.973, l: "8", a: 0}],
[{o: 0, f: 9.685, l: "a", a: 0},
{o: 9.685, f: 18.625, l: "b", a: 1},
{o: 18.625, f: 30.545, l: "b", a: 0},
{o: 30.545, f: 68.54, l: "c", a: 1},
{o: 68.54, f: 80.46, l: "b", a: 0},
{o: 80.46, f: 91.635, l: "b", a: 1},
{o: 91.635, f: 100.575, l: "b", a: 0},
{o: 100.575, f: 112.495, l: "b", a: 1},
{o: 112.495, f: 150.49, l: "c", a: 0},
{o: 150.49, f: 160.175, l: "b", a: 1}],
[{o: 0, f: 5.248, l: "n1", a: 0},
{o: 5.248, f: 17.264, l: "A", a: 1},
{o: 17.264, f: 29.547, l: "A", a: 0},
{o: 29.547, f: 67.628, l: "n2", a: 1},
{o: 67.628, f: 79.749, l: "B", a: 0},
{o: 79.749, f: 91.986, l: "B", a: 1},
{o: 91.986, f: 160.914, l: "n3", a: 0}],
[{o: 0, f: 0.016, l: "G", a: 0},
{o: 0.016, f: 40.364, l: "E", a: 1},
{o: 40.364, f: 52.72, l: "F", a: 0},
{o: 52.72, f: 82.34, l: "A", a: 1},
{o: 82.34, f: 96.2, l: "F", a: 0},
{o: 96.2, f: 117.596, l: "B", a: 1},
{o: 117.596, f: 147.372, l: "F", a: 0},
{o: 147.372, f: 160.716, l: "C", a: 1},
{o: 160.716, f: 161.075, l: "G", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000113.ogg";

var artist = "The Emotron";

var track = "Bigger Than JC";