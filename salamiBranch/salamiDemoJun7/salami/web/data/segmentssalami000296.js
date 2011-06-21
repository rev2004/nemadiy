var data = [
[{o: 3.483, f: 24.433, l: "A", a: 0},
{o: 24.433, f: 52.095, l: "B", a: 1},
{o: 52.095, f: 65.899, l: "C", a: 0},
{o: 65.899, f: 78.532, l: "D", a: 1},
{o: 78.532, f: 95.418, l: "C", a: 0},
{o: 95.418, f: 119.966, l: "B", a: 1},
{o: 119.966, f: 143.966, l: "C", a: 0},
{o: 143.966, f: 154.7, l: "A", a: 1},
{o: 154.7, f: 196.796, l: "E", a: 0}],
[{o: 0.312, f: 14.224, l: "A", a: 0},
{o: 14.224, f: 25.72, l: "A", a: 1},
{o: 25.72, f: 37.536, l: "B", a: 0},
{o: 37.536, f: 54.756, l: "A", a: 1},
{o: 54.756, f: 68.72, l: "A", a: 0},
{o: 68.72, f: 76.796, l: "A", a: 1},
{o: 76.796, f: 103.22, l: "A", a: 0},
{o: 103.22, f: 120.036, l: "A", a: 1},
{o: 120.036, f: 127.596, l: "A", a: 0},
{o: 127.596, f: 143.908, l: "A", a: 1},
{o: 143.908, f: 161.204, l: "A", a: 0},
{o: 161.204, f: 168.712, l: "A", a: 1},
{o: 168.712, f: 176.884, l: "A", a: 0},
{o: 176.884, f: 193.7, l: "A", a: 1}],
[{o: 0.312, f: 14.224, l: "A", a: 0},
{o: 14.224, f: 25.72, l: "C", a: 1},
{o: 25.72, f: 37.536, l: "F", a: 0},
{o: 37.536, f: 54.756, l: "A", a: 1},
{o: 54.756, f: 68.72, l: "C", a: 0},
{o: 68.72, f: 76.796, l: "A", a: 1},
{o: 76.796, f: 103.22, l: "B", a: 0},
{o: 103.22, f: 120.036, l: "B", a: 1},
{o: 120.036, f: 127.596, l: "C", a: 0},
{o: 127.596, f: 143.908, l: "D", a: 1},
{o: 143.908, f: 161.204, l: "E", a: 0},
{o: 161.204, f: 168.712, l: "G", a: 1},
{o: 168.712, f: 176.884, l: "E", a: 0},
{o: 176.884, f: 193.7, l: "D", a: 1}],
[{o: 0.653, f: 4.813, l: "8", a: 0},
{o: 4.813, f: 17.32, l: "4", a: 1},
{o: 17.32, f: 25.187, l: "3", a: 0},
{o: 25.187, f: 45.293, l: "1", a: 1},
{o: 45.293, f: 51.48, l: "3", a: 0},
{o: 51.48, f: 66.627, l: "2", a: 1},
{o: 66.627, f: 71.48, l: "3", a: 0},
{o: 71.48, f: 77.32, l: "1", a: 1},
{o: 77.32, f: 96.067, l: "2", a: 0},
{o: 96.067, f: 119.307, l: "1", a: 1},
{o: 119.307, f: 127.253, l: "2", a: 0},
{o: 127.253, f: 139.347, l: "5", a: 1},
{o: 139.347, f: 144.627, l: "7", a: 0},
{o: 144.627, f: 171.267, l: "1", a: 1},
{o: 171.267, f: 177.533, l: "4", a: 0},
{o: 177.533, f: 187.467, l: "1", a: 1},
{o: 187.467, f: 196.653, l: "6", a: 0}],
[{o: 0, f: 23.095, l: "a", a: 0},
{o: 23.095, f: 35.76, l: "b", a: 1},
{o: 35.76, f: 67.795, l: "c", a: 0},
{o: 67.795, f: 93.87, l: "c", a: 1},
{o: 93.87, f: 105.045, l: "b", a: 0},
{o: 105.045, f: 137.08, l: "c", a: 1},
{o: 137.08, f: 195.935, l: "d", a: 0}],
[{o: 0, f: 26.517, l: "n1", a: 0},
{o: 26.517, f: 42.225, l: "B", a: 1},
{o: 42.225, f: 70.24, l: "A", a: 0},
{o: 70.24, f: 97.268, l: "A", a: 1},
{o: 97.268, f: 111.096, l: "B", a: 0},
{o: 111.096, f: 137.3, l: "A", a: 1},
{o: 137.3, f: 196.58, l: "n5", a: 0}],
[{o: 0, f: 0.312, l: "F", a: 0},
{o: 0.312, f: 0.312, l: "B", a: 1},
{o: 0.312, f: 54.504, l: "A", a: 0},
{o: 54.504, f: 119.316, l: "D", a: 1},
{o: 119.316, f: 149.16, l: "C", a: 0},
{o: 149.16, f: 187.192, l: "E", a: 1},
{o: 187.192, f: 193.7, l: "C", a: 0},
{o: 193.7, f: 196.782, l: "F", a: 1}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000296.ogg";