var data = [
[{o: 0.313, f: 10.655, l: "A", a: 0},
{o: 10.655, f: 41.395, l: "B", a: 1},
{o: 41.395, f: 75.797, l: "C", a: 0},
{o: 75.797, f: 85.512, l: "A", a: 1},
{o: 85.512, f: 116.05, l: "B", a: 0},
{o: 116.05, f: 150.343, l: "C", a: 1},
{o: 150.343, f: 159.681, l: "A", a: 0},
{o: 159.681, f: 190.255, l: "B", a: 1},
{o: 190.255, f: 240.066, l: "C", a: 0}],
[{o: 0.232, f: 19.284, l: "B", a: 0},
{o: 19.284, f: 27.932, l: "B", a: 1},
{o: 27.932, f: 46.056, l: "B", a: 0},
{o: 46.056, f: 67.848, l: "B", a: 1},
{o: 67.848, f: 80.856, l: "B", a: 0},
{o: 80.856, f: 105.128, l: "C", a: 1},
{o: 105.128, f: 126.548, l: "B", a: 0},
{o: 126.548, f: 146.168, l: "B", a: 1},
{o: 146.168, f: 170.312, l: "C", a: 0},
{o: 170.312, f: 184.3, l: "A", a: 1},
{o: 184.3, f: 195.02, l: "B", a: 0},
{o: 195.02, f: 211.104, l: "B", a: 1},
{o: 211.104, f: 219.796, l: "B", a: 0},
{o: 219.796, f: 236.04, l: "D", a: 1}],
[{o: 0.232, f: 19.284, l: "B", a: 0},
{o: 19.284, f: 27.932, l: "D", a: 1},
{o: 27.932, f: 46.056, l: "A", a: 0},
{o: 46.056, f: 67.848, l: "A", a: 1},
{o: 67.848, f: 80.856, l: "A", a: 0},
{o: 80.856, f: 105.128, l: "C", a: 1},
{o: 105.128, f: 126.548, l: "A", a: 0},
{o: 126.548, f: 146.168, l: "A", a: 1},
{o: 146.168, f: 170.312, l: "C", a: 0},
{o: 170.312, f: 184.3, l: "E", a: 1},
{o: 184.3, f: 195.02, l: "A", a: 0},
{o: 195.02, f: 211.104, l: "F", a: 1},
{o: 211.104, f: 219.796, l: "G", a: 0},
{o: 219.796, f: 236.04, l: "H", a: 1}],
[{o: 0.547, f: 9, l: "4", a: 0},
{o: 9, f: 36.453, l: "1", a: 1},
{o: 36.453, f: 45.32, l: "2", a: 0},
{o: 45.32, f: 60.12, l: "3", a: 1},
{o: 60.12, f: 67.773, l: "5", a: 0},
{o: 67.773, f: 82.507, l: "2", a: 1},
{o: 82.507, f: 90.12, l: "1", a: 0},
{o: 90.12, f: 101, l: "2", a: 1},
{o: 101, f: 111.227, l: "1", a: 0},
{o: 111.227, f: 134.667, l: "3", a: 1},
{o: 134.667, f: 147.453, l: "6", a: 0},
{o: 147.453, f: 157.453, l: "4", a: 1},
{o: 157.453, f: 182.947, l: "1", a: 0},
{o: 182.947, f: 192.493, l: "2", a: 1},
{o: 192.493, f: 208.413, l: "3", a: 0},
{o: 208.413, f: 217.533, l: "5", a: 1},
{o: 217.533, f: 226.653, l: "8", a: 0},
{o: 226.653, f: 238.387, l: "7", a: 1}],
[{o: 0, f: 71.52, l: "a", a: 0},
{o: 71.52, f: 83.44, l: "b", a: 1},
{o: 83.44, f: 146.765, l: "a", a: 0},
{o: 146.765, f: 158.685, l: "b", a: 1},
{o: 158.685, f: 221.265, l: "a", a: 0},
{o: 221.265, f: 238.4, l: "c", a: 1},
{o: 238.4, f: 239.89, l: "d", a: 0}],
[{o: 0, f: 14.443, l: "n1", a: 0},
{o: 14.443, f: 57.899, l: "A", a: 1},
{o: 57.899, f: 62.555, l: "n2", a: 0},
{o: 62.555, f: 91.742, l: "B", a: 1},
{o: 91.742, f: 132.528, l: "A", a: 0},
{o: 132.528, f: 137.149, l: "n4", a: 1},
{o: 137.149, f: 166.069, l: "B", a: 0},
{o: 166.069, f: 206.913, l: "A", a: 1},
{o: 206.913, f: 240.048, l: "n6", a: 0}],
[{o: 0, f: 0.232, l: "E", a: 0},
{o: 0.232, f: 16.096, l: "D", a: 1},
{o: 16.096, f: 32.708, l: "C", a: 0},
{o: 32.708, f: 40.764, l: "D", a: 1},
{o: 40.764, f: 48.82, l: "B", a: 0},
{o: 48.82, f: 60.304, l: "D", a: 1},
{o: 60.304, f: 69.976, l: "B", a: 0},
{o: 69.976, f: 115.948, l: "D", a: 1},
{o: 115.948, f: 123.356, l: "B", a: 0},
{o: 123.356, f: 135.56, l: "D", a: 1},
{o: 135.56, f: 144.648, l: "B", a: 0},
{o: 144.648, f: 165.204, l: "D", a: 1},
{o: 165.204, f: 181.996, l: "C", a: 0},
{o: 181.996, f: 190.004, l: "D", a: 1},
{o: 190.004, f: 197.676, l: "B", a: 0},
{o: 197.676, f: 209.544, l: "D", a: 1},
{o: 209.544, f: 236.04, l: "B", a: 0},
{o: 236.04, f: 240.04, l: "E", a: 1}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami001072.ogg";
