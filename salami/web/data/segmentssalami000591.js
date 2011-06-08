var data = [
[{o: 0.044, f: 18.195, l: "A", a: 0},
{o: 18.195, f: 51.277, l: "B", a: 1},
{o: 51.277, f: 67.324, l: "C", a: 0},
{o: 67.324, f: 100.195, l: "D", a: 1},
{o: 100.195, f: 133.35, l: "B", a: 0},
{o: 133.35, f: 149.627, l: "C", a: 1},
{o: 149.627, f: 165.384, l: "E", a: 0},
{o: 165.384, f: 196.259, l: "F", a: 1},
{o: 196.259, f: 227.194, l: "G", a: 0},
{o: 227.194, f: 258.641, l: "C", a: 1},
{o: 258.641, f: 293.584, l: "H", a: 0},
{o: 293.584, f: 328.175, l: "Z", a: 1}],
[{o: 0.024, f: 10.504, l: "B", a: 0},
{o: 10.504, f: 18.676, l: "B", a: 1},
{o: 18.676, f: 35.7, l: "B", a: 0},
{o: 35.7, f: 54.04, l: "B", a: 1},
{o: 54.04, f: 70.976, l: "B", a: 0},
{o: 70.976, f: 99.54, l: "B", a: 1},
{o: 99.54, f: 112.052, l: "B", a: 0},
{o: 112.052, f: 131.096, l: "B", a: 1},
{o: 131.096, f: 148.02, l: "B", a: 0},
{o: 148.02, f: 163.692, l: "B", a: 1},
{o: 163.692, f: 174.396, l: "B", a: 0},
{o: 174.396, f: 192.276, l: "B", a: 1},
{o: 192.276, f: 209.656, l: "A", a: 0},
{o: 209.656, f: 227.488, l: "A", a: 1},
{o: 227.488, f: 242.66, l: "D", a: 0},
{o: 242.66, f: 270.268, l: "A", a: 1},
{o: 270.268, f: 286.4, l: "B", a: 0},
{o: 286.4, f: 301.992, l: "B", a: 1},
{o: 301.992, f: 313.48, l: "C", a: 0},
{o: 313.48, f: 327.988, l: "C", a: 1}],
[{o: 0.024, f: 10.504, l: "B", a: 0},
{o: 10.504, f: 18.676, l: "B", a: 1},
{o: 18.676, f: 35.7, l: "C", a: 0},
{o: 35.7, f: 54.04, l: "D", a: 1},
{o: 54.04, f: 70.976, l: "D", a: 0},
{o: 70.976, f: 99.54, l: "D", a: 1},
{o: 99.54, f: 112.052, l: "E", a: 0},
{o: 112.052, f: 131.096, l: "D", a: 1},
{o: 131.096, f: 148.02, l: "D", a: 0},
{o: 148.02, f: 163.692, l: "D", a: 1},
{o: 163.692, f: 174.396, l: "D", a: 0},
{o: 174.396, f: 192.276, l: "D", a: 1},
{o: 192.276, f: 209.656, l: "A", a: 0},
{o: 209.656, f: 227.488, l: "A", a: 1},
{o: 227.488, f: 242.66, l: "F", a: 0},
{o: 242.66, f: 270.268, l: "A", a: 1},
{o: 270.268, f: 286.4, l: "G", a: 0},
{o: 286.4, f: 301.992, l: "H", a: 1},
{o: 301.992, f: 313.48, l: "I", a: 0},
{o: 313.48, f: 327.988, l: "J", a: 1}],
[{o: 0.44, f: 18.96, l: "3", a: 0},
{o: 18.96, f: 45.68, l: "4", a: 1},
{o: 45.68, f: 66.653, l: "1", a: 0},
{o: 66.653, f: 73.333, l: "2", a: 1},
{o: 73.333, f: 99.867, l: "3", a: 0},
{o: 99.867, f: 108.187, l: "7", a: 1},
{o: 108.187, f: 128.32, l: "4", a: 0},
{o: 128.32, f: 147.76, l: "1", a: 1},
{o: 147.76, f: 165.853, l: "3", a: 0},
{o: 165.853, f: 175.96, l: "1", a: 1},
{o: 175.96, f: 218.08, l: "2", a: 0},
{o: 218.08, f: 235.693, l: "1", a: 1},
{o: 235.693, f: 276.533, l: "2", a: 0},
{o: 276.533, f: 292.267, l: "1", a: 1},
{o: 292.267, f: 303.467, l: "6", a: 0},
{o: 303.467, f: 310.72, l: "8", a: 1},
{o: 310.72, f: 327.96, l: "5", a: 0}],
[{o: 0, f: 68.54, l: "a", a: 0},
{o: 68.54, f: 93.125, l: "b", a: 1},
{o: 93.125, f: 150.49, l: "a", a: 0},
{o: 150.49, f: 275.65, l: "c", a: 1},
{o: 275.65, f: 287.57, l: "a", a: 0},
{o: 287.57, f: 327.8, l: "d", a: 1}],
[{o: 0, f: 1.985, l: "n1", a: 0},
{o: 1.985, f: 18.715, l: "C", a: 1},
{o: 18.715, f: 35.225, l: "D", a: 0},
{o: 35.225, f: 50.968, l: "D", a: 1},
{o: 50.968, f: 67.361, l: "A", a: 0},
{o: 67.361, f: 116.738, l: "n5", a: 1},
{o: 116.738, f: 133.12, l: "C", a: 0},
{o: 133.12, f: 149.548, l: "A", a: 1},
{o: 149.548, f: 165.175, l: "n6", a: 0},
{o: 165.175, f: 180.733, l: "B", a: 1},
{o: 180.733, f: 196.209, l: "B", a: 0},
{o: 196.209, f: 243.194, l: "n7", a: 1},
{o: 243.194, f: 259.019, l: "A", a: 0},
{o: 259.019, f: 327.726, l: "n8", a: 1}],
[{o: 0, f: 0.028, l: "I", a: 0},
{o: 0.028, f: 0.84, l: "G", a: 1},
{o: 0.84, f: 43.336, l: "H", a: 0},
{o: 43.336, f: 67.252, l: "G", a: 1},
{o: 67.252, f: 111.32, l: "H", a: 0},
{o: 111.32, f: 121.528, l: "G", a: 1},
{o: 121.528, f: 136.732, l: "H", a: 0},
{o: 136.732, f: 148.824, l: "G", a: 1},
{o: 148.824, f: 195.184, l: "H", a: 0},
{o: 195.184, f: 205.4, l: "G", a: 1},
{o: 205.4, f: 226.82, l: "H", a: 0},
{o: 226.82, f: 236.772, l: "G", a: 1},
{o: 236.772, f: 279.008, l: "H", a: 0},
{o: 279.008, f: 294.64, l: "G", a: 1},
{o: 294.64, f: 306.48, l: "F", a: 0},
{o: 306.48, f: 314.716, l: "H", a: 1},
{o: 314.716, f: 325.484, l: "F", a: 0},
{o: 325.484, f: 327.992, l: "H", a: 1},
{o: 327.992, f: 328.052, l: "I", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000591.ogg";

var artist = "Year Long Disaster";

var track = "Destination";
