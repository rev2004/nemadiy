var data = [
[{o: 0.058, f: 12.768, l: "Z", a: 0},
{o: 12.768, f: 39.865, l: "A", a: 1},
{o: 39.865, f: 75.314, l: "B", a: 0},
{o: 75.314, f: 91.763, l: "C", a: 1},
{o: 91.763, f: 110.265, l: "D", a: 0},
{o: 110.265, f: 127.869, l: "E", a: 1},
{o: 127.869, f: 163.286, l: "B", a: 0},
{o: 163.286, f: 179.741, l: "C", a: 1},
{o: 179.741, f: 198.251, l: "D", a: 0},
{o: 198.251, f: 233.435, l: "E", a: 1},
{o: 233.435, f: 288.047, l: "F", a: 0}],
[{o: 0.148, f: 16.012, l: "B", a: 0},
{o: 16.012, f: 28.368, l: "B", a: 1},
{o: 28.368, f: 50.416, l: "D", a: 0},
{o: 50.416, f: 65.784, l: "D", a: 1},
{o: 65.784, f: 80.768, l: "D", a: 0},
{o: 80.768, f: 91.984, l: "D", a: 1},
{o: 91.984, f: 110.576, l: "D", a: 0},
{o: 110.576, f: 130.156, l: "D", a: 1},
{o: 130.156, f: 142.556, l: "D", a: 0},
{o: 142.556, f: 153.452, l: "D", a: 1},
{o: 153.452, f: 162.008, l: "D", a: 0},
{o: 162.008, f: 179.964, l: "D", a: 1},
{o: 179.964, f: 199.12, l: "D", a: 0},
{o: 199.12, f: 219.924, l: "D", a: 1},
{o: 219.924, f: 234.868, l: "D", a: 0},
{o: 234.868, f: 257.24, l: "D", a: 1},
{o: 257.24, f: 273.656, l: "D", a: 0},
{o: 273.656, f: 284.604, l: "D", a: 1}],
[{o: 0.148, f: 16.012, l: "B", a: 0},
{o: 16.012, f: 28.368, l: "C", a: 1},
{o: 28.368, f: 50.416, l: "D", a: 0},
{o: 50.416, f: 65.784, l: "D", a: 1},
{o: 65.784, f: 80.768, l: "D", a: 0},
{o: 80.768, f: 91.984, l: "D", a: 1},
{o: 91.984, f: 110.576, l: "A", a: 0},
{o: 110.576, f: 130.156, l: "E", a: 1},
{o: 130.156, f: 142.556, l: "D", a: 0},
{o: 142.556, f: 153.452, l: "D", a: 1},
{o: 153.452, f: 162.008, l: "F", a: 0},
{o: 162.008, f: 179.964, l: "D", a: 1},
{o: 179.964, f: 199.12, l: "A", a: 0},
{o: 199.12, f: 219.924, l: "G", a: 1},
{o: 219.924, f: 234.868, l: "A", a: 0},
{o: 234.868, f: 257.24, l: "A", a: 1},
{o: 257.24, f: 273.656, l: "A", a: 0},
{o: 273.656, f: 284.604, l: "H", a: 1}],
[{o: 0.76, f: 6.747, l: "5", a: 0},
{o: 6.747, f: 13.693, l: "8", a: 1},
{o: 13.693, f: 20.76, l: "5", a: 0},
{o: 20.76, f: 39.467, l: "1", a: 1},
{o: 39.467, f: 77.533, l: "2", a: 0},
{o: 77.533, f: 84.04, l: "4", a: 1},
{o: 84.04, f: 91.4, l: "2", a: 0},
{o: 91.4, f: 128.453, l: "1", a: 1},
{o: 128.453, f: 164.347, l: "2", a: 0},
{o: 164.347, f: 172.6, l: "4", a: 1},
{o: 172.6, f: 198.813, l: "1", a: 0},
{o: 198.813, f: 233.12, l: "3", a: 1},
{o: 233.12, f: 270.667, l: "1", a: 0},
{o: 270.667, f: 277.56, l: "7", a: 1},
{o: 277.56, f: 287.347, l: "6", a: 0}],
[{o: 0, f: 42.465, l: "a", a: 0},
{o: 42.465, f: 125.16, l: "b", a: 1},
{o: 125.16, f: 213.815, l: "b", a: 0},
{o: 213.815, f: 232.44, l: "c", a: 1},
{o: 232.44, f: 251.81, l: "d", a: 0},
{o: 251.81, f: 271.18, l: "d", a: 1},
{o: 271.18, f: 287.57, l: "e", a: 0}],
[{o: 0, f: 25.484, l: "n1", a: 0},
{o: 25.484, f: 34.238, l: "A", a: 1},
{o: 34.238, f: 43.085, l: "A", a: 0},
{o: 43.085, f: 51.955, l: "A", a: 1},
{o: 51.955, f: 60.813, l: "A", a: 0},
{o: 60.813, f: 69.033, l: "A", a: 1},
{o: 69.033, f: 99.602, l: "B", a: 0},
{o: 99.602, f: 105.686, l: "n6", a: 1},
{o: 105.686, f: 116.169, l: "D", a: 0},
{o: 116.169, f: 139.947, l: "n7", a: 1},
{o: 139.947, f: 148.167, l: "A", a: 0},
{o: 148.167, f: 157.025, l: "n8", a: 1},
{o: 157.025, f: 187.524, l: "B", a: 0},
{o: 187.524, f: 193.62, l: "n9", a: 1},
{o: 193.62, f: 204.115, l: "D", a: 0},
{o: 204.115, f: 233.697, l: "n10", a: 1},
{o: 233.697, f: 252.796, l: "C", a: 0},
{o: 252.796, f: 271.859, l: "C", a: 1},
{o: 271.859, f: 287.602, l: "n11", a: 0}],
[{o: 0, f: 0.148, l: "E", a: 0},
{o: 0.148, f: 12.804, l: "B", a: 1},
{o: 12.804, f: 85.788, l: "C", a: 0},
{o: 85.788, f: 109.688, l: "D", a: 1},
{o: 109.688, f: 173.468, l: "C", a: 0},
{o: 173.468, f: 197.644, l: "D", a: 1},
{o: 197.644, f: 269.812, l: "C", a: 0},
{o: 269.812, f: 283.536, l: "D", a: 1},
{o: 283.536, f: 287.893, l: "E", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami001546.ogg";

var artist = "Brushfire Stankgrass";

var track = "Panthertown";
