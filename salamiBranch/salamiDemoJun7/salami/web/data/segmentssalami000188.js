var data = [
[{o: 0.039, f: 13.417, l: "A", a: 0},
{o: 13.417, f: 25.946, l: "A", a: 1},
{o: 25.946, f: 38.495, l: "B", a: 0},
{o: 38.495, f: 57.623, l: "A", a: 1},
{o: 57.623, f: 70.401, l: "B", a: 0},
{o: 70.401, f: 90.07, l: "A", a: 1},
{o: 90.07, f: 115.769, l: "C", a: 0},
{o: 115.769, f: 142.211, l: "D", a: 1},
{o: 142.211, f: 169.386, l: "D", a: 0},
{o: 169.386, f: 182.135, l: "A", a: 1},
{o: 182.135, f: 194.718, l: "B", a: 0},
{o: 194.718, f: 215.763, l: "A", a: 1}],
[{o: 0.004, f: 12.296, l: "B", a: 0},
{o: 12.296, f: 20.58, l: "B", a: 1},
{o: 20.58, f: 44.116, l: "B", a: 0},
{o: 44.116, f: 56.504, l: "B", a: 1},
{o: 56.504, f: 67.796, l: "A", a: 0},
{o: 67.796, f: 81.86, l: "B", a: 1},
{o: 81.86, f: 89.7, l: "B", a: 0},
{o: 89.7, f: 109.784, l: "A", a: 1},
{o: 109.784, f: 125.028, l: "B", a: 0},
{o: 125.028, f: 140.464, l: "B", a: 1},
{o: 140.464, f: 166.5, l: "B", a: 0},
{o: 166.5, f: 176.568, l: "B", a: 1},
{o: 176.568, f: 187.184, l: "B", a: 0},
{o: 187.184, f: 206.748, l: "B", a: 1},
{o: 206.748, f: 215.272, l: "B", a: 0}],
[{o: 0.004, f: 12.296, l: "D", a: 0},
{o: 12.296, f: 20.58, l: "B", a: 1},
{o: 20.58, f: 44.116, l: "C", a: 0},
{o: 44.116, f: 56.504, l: "C", a: 1},
{o: 56.504, f: 67.796, l: "F", a: 0},
{o: 67.796, f: 81.86, l: "C", a: 1},
{o: 81.86, f: 89.7, l: "A", a: 0},
{o: 89.7, f: 109.784, l: "G", a: 1},
{o: 109.784, f: 125.028, l: "E", a: 0},
{o: 125.028, f: 140.464, l: "E", a: 1},
{o: 140.464, f: 166.5, l: "B", a: 0},
{o: 166.5, f: 176.568, l: "C", a: 1},
{o: 176.568, f: 187.184, l: "A", a: 0},
{o: 187.184, f: 206.748, l: "C", a: 1},
{o: 206.748, f: 215.272, l: "C", a: 0}],
[{o: 0.387, f: 6.827, l: "1", a: 0},
{o: 6.827, f: 27.093, l: "2", a: 1},
{o: 27.093, f: 34.253, l: "4", a: 0},
{o: 34.253, f: 44.467, l: "1", a: 1},
{o: 44.467, f: 49.787, l: "2", a: 0},
{o: 49.787, f: 58.36, l: "3", a: 1},
{o: 58.36, f: 66.613, l: "4", a: 0},
{o: 66.613, f: 81.16, l: "1", a: 1},
{o: 81.16, f: 87.427, l: "3", a: 0},
{o: 87.427, f: 112.787, l: "7", a: 1},
{o: 112.787, f: 126.32, l: "5", a: 0},
{o: 126.32, f: 134.547, l: "6", a: 1},
{o: 134.547, f: 143.053, l: "1", a: 0},
{o: 143.053, f: 151.773, l: "5", a: 1},
{o: 151.773, f: 161.56, l: "6", a: 0},
{o: 161.56, f: 175.307, l: "1", a: 1},
{o: 175.307, f: 183.32, l: "2", a: 0},
{o: 183.32, f: 191.053, l: "4", a: 1},
{o: 191.053, f: 200.707, l: "1", a: 0},
{o: 200.707, f: 212.027, l: "3", a: 1},
{o: 212.027, f: 215.427, l: "8", a: 0}],
[{o: 0, f: 11.92, l: "a", a: 0},
{o: 11.92, f: 28.31, l: "a", a: 1},
{o: 28.31, f: 37.995, l: "b", a: 0},
{o: 37.995, f: 58.11, l: "a", a: 1},
{o: 58.11, f: 74.5, l: "a", a: 0},
{o: 74.5, f: 90.145, l: "a", a: 1},
{o: 90.145, f: 102.81, l: "c", a: 0},
{o: 102.81, f: 115.475, l: "c", a: 1},
{o: 115.475, f: 141.55, l: "d", a: 0},
{o: 141.55, f: 168.37, l: "d", a: 1},
{o: 168.37, f: 184.76, l: "a", a: 0},
{o: 184.76, f: 215.305, l: "a", a: 1}],
[{o: 0, f: 12.992, l: "n1", a: 0},
{o: 12.992, f: 38.011, l: "A", a: 1},
{o: 38.011, f: 58.12, l: "A", a: 0},
{o: 58.12, f: 69.892, l: "n3", a: 1},
{o: 69.892, f: 89.606, l: "A", a: 0},
{o: 89.606, f: 118.538, l: "n4", a: 1},
{o: 118.538, f: 144.37, l: "B", a: 0},
{o: 144.37, f: 168.867, l: "B", a: 1},
{o: 168.867, f: 189.277, l: "A", a: 0},
{o: 189.277, f: 215.528, l: "n6", a: 1}],
[{o: 0, f: 0.004, l: "F", a: 0},
{o: 0.004, f: 93.448, l: "D", a: 1},
{o: 93.448, f: 135.844, l: "A", a: 0},
{o: 135.844, f: 206.22, l: "E", a: 1},
{o: 206.22, f: 215.272, l: "B", a: 0},
{o: 215.272, f: 215.746, l: "F", a: 1}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000188.ogg";