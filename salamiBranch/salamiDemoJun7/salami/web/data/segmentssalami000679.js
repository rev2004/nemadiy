var data = [
[{o: 0.047, f: 69.777, l: "A", a: 0},
{o: 69.777, f: 120.523, l: "B", a: 1},
{o: 120.523, f: 169.467, l: "C", a: 0},
{o: 169.467, f: 209.436, l: "B", a: 1},
{o: 209.436, f: 225.056, l: "Z", a: 0}],
[{o: 0.08, f: 10.276, l: "E", a: 0},
{o: 10.276, f: 21.564, l: "E", a: 1},
{o: 21.564, f: 29.244, l: "A", a: 0},
{o: 29.244, f: 39.144, l: "E", a: 1},
{o: 39.144, f: 53.932, l: "E", a: 0},
{o: 53.932, f: 72.64, l: "E", a: 1},
{o: 72.64, f: 79.884, l: "E", a: 0},
{o: 79.884, f: 89.924, l: "E", a: 1},
{o: 89.924, f: 110.94, l: "E", a: 0},
{o: 110.94, f: 126.124, l: "E", a: 1},
{o: 126.124, f: 139.636, l: "E", a: 0},
{o: 139.636, f: 154.764, l: "E", a: 1},
{o: 154.764, f: 164.068, l: "E", a: 0},
{o: 164.068, f: 172.86, l: "E", a: 1},
{o: 172.86, f: 183.796, l: "E", a: 0},
{o: 183.796, f: 193.736, l: "E", a: 1},
{o: 193.736, f: 210.888, l: "E", a: 0},
{o: 210.888, f: 224.908, l: "E", a: 1}],
[{o: 0.08, f: 10.276, l: "E", a: 0},
{o: 10.276, f: 21.564, l: "E", a: 1},
{o: 21.564, f: 29.244, l: "F", a: 0},
{o: 29.244, f: 39.144, l: "C", a: 1},
{o: 39.144, f: 53.932, l: "A", a: 0},
{o: 53.932, f: 72.64, l: "D", a: 1},
{o: 72.64, f: 79.884, l: "A", a: 0},
{o: 79.884, f: 89.924, l: "A", a: 1},
{o: 89.924, f: 110.94, l: "A", a: 0},
{o: 110.94, f: 126.124, l: "A", a: 1},
{o: 126.124, f: 139.636, l: "A", a: 0},
{o: 139.636, f: 154.764, l: "D", a: 1},
{o: 154.764, f: 164.068, l: "A", a: 0},
{o: 164.068, f: 172.86, l: "B", a: 1},
{o: 172.86, f: 183.796, l: "B", a: 0},
{o: 183.796, f: 193.736, l: "G", a: 1},
{o: 193.736, f: 210.888, l: "H", a: 0},
{o: 210.888, f: 224.908, l: "C", a: 1}],
[{o: 0.813, f: 33.84, l: "3", a: 0},
{o: 33.84, f: 45.173, l: "5", a: 1},
{o: 45.173, f: 89.027, l: "2", a: 0},
{o: 89.027, f: 191.053, l: "1", a: 1},
{o: 191.053, f: 199.373, l: "7", a: 0},
{o: 199.373, f: 207.853, l: "6", a: 1},
{o: 207.853, f: 221.267, l: "4", a: 0},
{o: 221.267, f: 224.68, l: "8", a: 1}],
[{o: 0, f: 17.88, l: "a", a: 0},
{o: 17.88, f: 29.055, l: "a", a: 1},
{o: 29.055, f: 40.975, l: "a", a: 0},
{o: 40.975, f: 61.835, l: "a", a: 1},
{o: 61.835, f: 81.95, l: "a", a: 0},
{o: 81.95, f: 108.025, l: "a", a: 1},
{o: 108.025, f: 128.14, l: "a", a: 0},
{o: 128.14, f: 152.725, l: "a", a: 1},
{o: 152.725, f: 172.84, l: "a", a: 0},
{o: 172.84, f: 192.21, l: "a", a: 1},
{o: 192.21, f: 224.245, l: "b", a: 0}],
[{o: 0, f: 3.692, l: "n1", a: 0},
{o: 3.692, f: 42.33, l: "A", a: 1},
{o: 42.33, f: 54.892, l: "n2", a: 0},
{o: 54.892, f: 92.497, l: "A", a: 1},
{o: 92.497, f: 105.024, l: "n3", a: 0},
{o: 105.024, f: 142.129, l: "A", a: 1},
{o: 142.129, f: 154.796, l: "n4", a: 0},
{o: 154.796, f: 192.505, l: "A", a: 1},
{o: 192.505, f: 224.816, l: "n5", a: 0}],
[{o: 0, f: 0.08, l: "E", a: 0},
{o: 0.08, f: 192.168, l: "B", a: 1},
{o: 192.168, f: 206.068, l: "D", a: 0},
{o: 206.068, f: 224.908, l: "A", a: 1},
{o: 224.908, f: 224.955, l: "E", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000679.ogg";