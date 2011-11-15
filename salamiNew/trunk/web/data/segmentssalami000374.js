var data = [
[{o: 0.372, f: 33.057, l: "A", a: 0},
{o: 33.057, f: 55.204, l: "B", a: 1},
{o: 55.204, f: 60.717, l: "C", a: 0},
{o: 60.717, f: 82.451, l: "B", a: 1},
{o: 82.451, f: 87.966, l: "C", a: 0},
{o: 87.966, f: 101.646, l: "D", a: 1},
{o: 101.646, f: 123.354, l: "B", a: 0},
{o: 123.354, f: 128.917, l: "C", a: 1},
{o: 128.917, f: 156.068, l: "E", a: 0},
{o: 156.068, f: 169.777, l: "D", a: 1},
{o: 169.777, f: 191.59, l: "B", a: 0},
{o: 191.59, f: 208.003, l: "C", a: 1},
{o: 208.003, f: 274.029, l: "A", a: 0}],
[{o: 0.208, f: 10.516, l: "D", a: 0},
{o: 10.516, f: 25.876, l: "E", a: 1},
{o: 25.876, f: 41.232, l: "C", a: 0},
{o: 41.232, f: 60.336, l: "C", a: 1},
{o: 60.336, f: 75.996, l: "C", a: 0},
{o: 75.996, f: 96.104, l: "A", a: 1},
{o: 96.104, f: 111.768, l: "A", a: 0},
{o: 111.768, f: 128.864, l: "A", a: 1},
{o: 128.864, f: 142.78, l: "A", a: 0},
{o: 142.78, f: 155.052, l: "A", a: 1},
{o: 155.052, f: 168.696, l: "F", a: 0},
{o: 168.696, f: 184.056, l: "A", a: 1},
{o: 184.056, f: 196.348, l: "A", a: 0},
{o: 196.348, f: 208.26, l: "B", a: 1},
{o: 208.26, f: 229.712, l: "B", a: 0},
{o: 229.712, f: 245.06, l: "B", a: 1},
{o: 245.06, f: 261.096, l: "B", a: 0},
{o: 261.096, f: 270.948, l: "G", a: 1}],
[{o: 0.208, f: 10.516, l: "D", a: 0},
{o: 10.516, f: 25.876, l: "E", a: 1},
{o: 25.876, f: 41.232, l: "C", a: 0},
{o: 41.232, f: 60.336, l: "C", a: 1},
{o: 60.336, f: 75.996, l: "C", a: 0},
{o: 75.996, f: 96.104, l: "A", a: 1},
{o: 96.104, f: 111.768, l: "A", a: 0},
{o: 111.768, f: 128.864, l: "A", a: 1},
{o: 128.864, f: 142.78, l: "F", a: 0},
{o: 142.78, f: 155.052, l: "G", a: 1},
{o: 155.052, f: 168.696, l: "H", a: 0},
{o: 168.696, f: 184.056, l: "A", a: 1},
{o: 184.056, f: 196.348, l: "A", a: 0},
{o: 196.348, f: 208.26, l: "I", a: 1},
{o: 208.26, f: 229.712, l: "B", a: 0},
{o: 229.712, f: 245.06, l: "B", a: 1},
{o: 245.06, f: 261.096, l: "J", a: 0},
{o: 261.096, f: 270.948, l: "K", a: 1}],
[{o: 0.307, f: 32.533, l: "5", a: 0},
{o: 32.533, f: 54.347, l: "1", a: 1},
{o: 54.347, f: 58.453, l: "2", a: 0},
{o: 58.453, f: 62.52, l: "1", a: 1},
{o: 62.52, f: 76.853, l: "3", a: 0},
{o: 76.853, f: 81.627, l: "1", a: 1},
{o: 81.627, f: 88.12, l: "2", a: 0},
{o: 88.12, f: 100.387, l: "4", a: 1},
{o: 100.387, f: 112.293, l: "1", a: 0},
{o: 112.293, f: 118.427, l: "3", a: 1},
{o: 118.427, f: 122.88, l: "1", a: 0},
{o: 122.88, f: 129.013, l: "2", a: 1},
{o: 129.013, f: 156.293, l: "6", a: 0},
{o: 156.293, f: 168.88, l: "4", a: 1},
{o: 168.88, f: 180.133, l: "1", a: 0},
{o: 180.133, f: 184.907, l: "3", a: 1},
{o: 184.907, f: 190.693, l: "1", a: 0},
{o: 190.693, f: 259.547, l: "2", a: 1},
{o: 259.547, f: 269.747, l: "7", a: 0},
{o: 269.747, f: 273.88, l: "8", a: 1}],
[{o: 0, f: 68.54, l: "a", a: 0},
{o: 68.54, f: 87.165, l: "b", a: 1},
{o: 87.165, f: 110.26, l: "c", a: 0},
{o: 110.26, f: 128.14, l: "b", a: 1},
{o: 128.14, f: 178.8, l: "d", a: 0},
{o: 178.8, f: 196.68, l: "b", a: 1},
{o: 196.68, f: 273.415, l: "e", a: 0}],
[{o: 0, f: 1.358, l: "n1", a: 0},
{o: 1.358, f: 12.272, l: "B", a: 1},
{o: 12.272, f: 23.162, l: "C", a: 0},
{o: 23.162, f: 32.717, l: "C", a: 1},
{o: 32.717, f: 45, l: "n4", a: 0},
{o: 45, f: 54.544, l: "A", a: 1},
{o: 54.544, f: 72.284, l: "n5", a: 0},
{o: 72.284, f: 81.816, l: "A", a: 1},
{o: 81.816, f: 140.434, l: "n6", a: 0},
{o: 140.434, f: 149.966, l: "A", a: 1},
{o: 149.966, f: 181.359, l: "n7", a: 0},
{o: 181.359, f: 190.891, l: "A", a: 1},
{o: 190.891, f: 219.521, l: "n8", a: 0},
{o: 219.521, f: 229.065, l: "B", a: 1},
{o: 229.065, f: 273.856, l: "n9", a: 0}],
[{o: 0, f: 0.208, l: "E", a: 0},
{o: 0.208, f: 251.876, l: "C", a: 1},
{o: 251.876, f: 270.948, l: "D", a: 0},
{o: 270.948, f: 273.999, l: "E", a: 1}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000374.ogg";

var artist = "Dawn Tyler Blues Project";

var track = "Latex";
