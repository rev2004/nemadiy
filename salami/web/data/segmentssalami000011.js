var data = [
[{o: 0.07, f: 31.834, l: "M", a: 0},
{o: 31.834, f: 67.097, l: "E", a: 1},
{o: 67.097, f: 84.732, l: "I", a: 0},
{o: 84.732, f: 105.925, l: "L", a: 1},
{o: 105.925, f: 141.188, l: "E", a: 0},
{o: 141.188, f: 158.87, l: "I", a: 1},
{o: 158.87, f: 173.005, l: "C", a: 0},
{o: 173.005, f: 208.273, l: "E", a: 1},
{o: 208.273, f: 222.529, l: "I", a: 0},
{o: 222.529, f: 270.126, l: "M", a: 1}],
[{o: 0.008, f: 5.304, l: "B", a: 0},
{o: 5.304, f: 13.248, l: "A", a: 1},
{o: 13.248, f: 23.824, l: "B", a: 0},
{o: 23.824, f: 32.644, l: "B", a: 1},
{o: 32.644, f: 46.78, l: "B", a: 0},
{o: 46.78, f: 62.216, l: "B", a: 1},
{o: 62.216, f: 76.312, l: "B", a: 0},
{o: 76.312, f: 84.264, l: "B", a: 1},
{o: 84.264, f: 96.612, l: "B", a: 0},
{o: 96.612, f: 106.324, l: "B", a: 1},
{o: 106.324, f: 123.072, l: "B", a: 0},
{o: 123.072, f: 134.568, l: "B", a: 1},
{o: 134.568, f: 142.492, l: "B", a: 0},
{o: 142.492, f: 151.756, l: "B", a: 1},
{o: 151.756, f: 158.804, l: "C", a: 0},
{o: 158.804, f: 176.452, l: "B", a: 1},
{o: 176.452, f: 196.744, l: "B", a: 0},
{o: 196.744, f: 207.772, l: "D", a: 1},
{o: 207.772, f: 223.656, l: "B", a: 0},
{o: 223.656, f: 235.572, l: "E", a: 1},
{o: 235.572, f: 253.084, l: "F", a: 0},
{o: 253.084, f: 263.68, l: "G", a: 1}],
[{o: 0.008, f: 5.304, l: "E", a: 0},
{o: 5.304, f: 13.248, l: "F", a: 1},
{o: 13.248, f: 23.824, l: "G", a: 0},
{o: 23.824, f: 32.644, l: "A", a: 1},
{o: 32.644, f: 46.78, l: "C", a: 0},
{o: 46.78, f: 62.216, l: "C", a: 1},
{o: 62.216, f: 76.312, l: "B", a: 0},
{o: 76.312, f: 84.264, l: "C", a: 1},
{o: 84.264, f: 96.612, l: "D", a: 0},
{o: 96.612, f: 106.324, l: "A", a: 1},
{o: 106.324, f: 123.072, l: "C", a: 0},
{o: 123.072, f: 134.568, l: "D", a: 1},
{o: 134.568, f: 142.492, l: "D", a: 0},
{o: 142.492, f: 151.756, l: "B", a: 1},
{o: 151.756, f: 158.804, l: "H", a: 0},
{o: 158.804, f: 176.452, l: "C", a: 1},
{o: 176.452, f: 196.744, l: "C", a: 0},
{o: 196.744, f: 207.772, l: "I", a: 1},
{o: 207.772, f: 223.656, l: "J", a: 0},
{o: 223.656, f: 235.572, l: "K", a: 1},
{o: 235.572, f: 253.084, l: "L", a: 0},
{o: 253.084, f: 263.68, l: "M", a: 1}],
[{o: 0.467, f: 4.213, l: "3", a: 0},
{o: 4.213, f: 16.56, l: "6", a: 1},
{o: 16.56, f: 23.627, l: "2", a: 0},
{o: 23.627, f: 28.013, l: "3", a: 1},
{o: 28.013, f: 35.947, l: "2", a: 0},
{o: 35.947, f: 43.04, l: "4", a: 1},
{o: 43.04, f: 51.853, l: "1", a: 0},
{o: 51.853, f: 64.2, l: "5", a: 1},
{o: 64.2, f: 80.547, l: "1", a: 0},
{o: 80.547, f: 85.813, l: "7", a: 1},
{o: 85.813, f: 95.067, l: "4", a: 0},
{o: 95.067, f: 102.12, l: "3", a: 1},
{o: 102.12, f: 109.187, l: "2", a: 0},
{o: 109.187, f: 116.707, l: "4", a: 1},
{o: 116.707, f: 129.48, l: "1", a: 0},
{o: 129.48, f: 139.64, l: "5", a: 1},
{o: 139.64, f: 148.893, l: "1", a: 0},
{o: 148.893, f: 154.613, l: "3", a: 1},
{o: 154.613, f: 164.347, l: "2", a: 0},
{o: 164.347, f: 169.173, l: "3", a: 1},
{o: 169.173, f: 176.68, l: "2", a: 0},
{o: 176.68, f: 184.187, l: "4", a: 1},
{o: 184.187, f: 193, l: "1", a: 0},
{o: 193, f: 204.933, l: "5", a: 1},
{o: 204.933, f: 216.387, l: "1", a: 0},
{o: 216.387, f: 221.68, l: "3", a: 1},
{o: 221.68, f: 228.733, l: "7", a: 0},
{o: 228.733, f: 238.88, l: "6", a: 1},
{o: 238.88, f: 245.947, l: "2", a: 0},
{o: 245.947, f: 250.227, l: "3", a: 1},
{o: 250.227, f: 257.053, l: "2", a: 0},
{o: 257.053, f: 268.6, l: "8", a: 1}],
[{o: 0, f: 15.645, l: "a", a: 0},
{o: 15.645, f: 36.505, l: "b", a: 1},
{o: 36.505, f: 82.695, l: "c", a: 0},
{o: 82.695, f: 111.005, l: "d", a: 1},
{o: 111.005, f: 157.195, l: "c", a: 0},
{o: 157.195, f: 178.055, l: "b", a: 1},
{o: 178.055, f: 224.99, l: "c", a: 0},
{o: 224.99, f: 269.69, l: "d", a: 1}],
[{o: 0, f: 3.564, l: "n1", a: 0},
{o: 3.564, f: 23.417, l: "B", a: 1},
{o: 23.417, f: 79.9, l: "A", a: 0},
{o: 79.9, f: 97.524, l: "n3", a: 1},
{o: 97.524, f: 153.995, l: "A", a: 0},
{o: 153.995, f: 164.571, l: "n4", a: 1},
{o: 164.571, f: 225.895, l: "A", a: 0},
{o: 225.895, f: 245.307, l: "B", a: 1},
{o: 245.307, f: 269.769, l: "n6", a: 0}],
[{o: 0, f: 0.004, l: "J", a: 0},
{o: 0.004, f: 0.004, l: "B", a: 1},
{o: 0.004, f: 14.352, l: "A", a: 0},
{o: 14.352, f: 26.696, l: "I", a: 1},
{o: 26.696, f: 235.356, l: "E", a: 0},
{o: 235.356, f: 250.776, l: "I", a: 1},
{o: 250.776, f: 263.5, l: "E", a: 0},
{o: 263.5, f: 270.08, l: "J", a: 1}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000011.ogg";
