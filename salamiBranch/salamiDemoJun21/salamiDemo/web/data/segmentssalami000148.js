var data = [
[{o: 0.284, f: 7.727, l: "A", a: 0},
{o: 7.727, f: 15.481, l: "X", a: 1},
{o: 15.481, f: 23.637, l: "B", a: 0},
{o: 23.637, f: 31.841, l: "X", a: 1},
{o: 31.841, f: 39.963, l: "X", a: 0},
{o: 39.963, f: 48.453, l: "X", a: 1},
{o: 48.453, f: 56.771, l: "B", a: 0},
{o: 56.771, f: 65.043, l: "X", a: 1},
{o: 65.043, f: 73.222, l: "X", a: 0},
{o: 73.222, f: 81.562, l: "X", a: 1},
{o: 81.562, f: 89.796, l: "C", a: 0},
{o: 89.796, f: 98.09, l: "X", a: 1},
{o: 98.09, f: 106.295, l: "X", a: 0},
{o: 106.295, f: 114.68, l: "X", a: 1},
{o: 114.68, f: 122.879, l: "B", a: 0},
{o: 122.879, f: 131.419, l: "X", a: 1},
{o: 131.419, f: 139.787, l: "X", a: 0},
{o: 139.787, f: 148.314, l: "X", a: 1},
{o: 148.314, f: 156.797, l: "C", a: 0},
{o: 156.797, f: 165.173, l: "X", a: 1},
{o: 165.173, f: 173.386, l: "X", a: 0},
{o: 173.386, f: 181.809, l: "X", a: 1},
{o: 181.809, f: 190.256, l: "B", a: 0},
{o: 190.256, f: 198.74, l: "X", a: 1},
{o: 198.74, f: 207.174, l: "X", a: 0},
{o: 207.174, f: 215.734, l: "X", a: 1},
{o: 215.734, f: 224.248, l: "A", a: 0},
{o: 224.248, f: 238.318, l: "X", a: 1}],
[{o: 0.176, f: 13.34, l: "A", a: 0},
{o: 13.34, f: 21.576, l: "B", a: 1},
{o: 21.576, f: 31.84, l: "A", a: 0},
{o: 31.84, f: 42.656, l: "A", a: 1},
{o: 42.656, f: 55.192, l: "A", a: 0},
{o: 55.192, f: 69.132, l: "A", a: 1},
{o: 69.132, f: 80.392, l: "A", a: 0},
{o: 80.392, f: 87.2, l: "C", a: 1},
{o: 87.2, f: 107.788, l: "A", a: 0},
{o: 107.788, f: 115.688, l: "D", a: 1},
{o: 115.688, f: 127.316, l: "A", a: 0},
{o: 127.316, f: 136.26, l: "A", a: 1},
{o: 136.26, f: 145.204, l: "A", a: 0},
{o: 145.204, f: 166.296, l: "A", a: 1},
{o: 166.296, f: 174.568, l: "A", a: 0},
{o: 174.568, f: 191.884, l: "A", a: 1},
{o: 191.884, f: 203.532, l: "A", a: 0},
{o: 203.532, f: 216.456, l: "A", a: 1},
{o: 216.456, f: 235.572, l: "A", a: 0}],
[{o: 0.176, f: 13.34, l: "A", a: 0},
{o: 13.34, f: 21.576, l: "D", a: 1},
{o: 21.576, f: 31.84, l: "C", a: 0},
{o: 31.84, f: 42.656, l: "C", a: 1},
{o: 42.656, f: 55.192, l: "C", a: 0},
{o: 55.192, f: 69.132, l: "C", a: 1},
{o: 69.132, f: 80.392, l: "B", a: 0},
{o: 80.392, f: 87.2, l: "E", a: 1},
{o: 87.2, f: 107.788, l: "B", a: 0},
{o: 107.788, f: 115.688, l: "F", a: 1},
{o: 115.688, f: 127.316, l: "C", a: 0},
{o: 127.316, f: 136.26, l: "C", a: 1},
{o: 136.26, f: 145.204, l: "C", a: 0},
{o: 145.204, f: 166.296, l: "C", a: 1},
{o: 166.296, f: 174.568, l: "G", a: 0},
{o: 174.568, f: 191.884, l: "C", a: 1},
{o: 191.884, f: 203.532, l: "H", a: 0},
{o: 203.532, f: 216.456, l: "I", a: 1},
{o: 216.456, f: 235.572, l: "J", a: 0}],
[{o: 0.92, f: 8.96, l: "3", a: 0},
{o: 8.96, f: 19.227, l: "1", a: 1},
{o: 19.227, f: 27.467, l: "6", a: 0},
{o: 27.467, f: 62.667, l: "1", a: 1},
{o: 62.667, f: 69.413, l: "7", a: 0},
{o: 69.413, f: 75.52, l: "1", a: 1},
{o: 75.52, f: 113.92, l: "4", a: 0},
{o: 113.92, f: 124.227, l: "2", a: 1},
{o: 124.227, f: 135.933, l: "5", a: 0},
{o: 135.933, f: 146.56, l: "1", a: 1},
{o: 146.56, f: 160.213, l: "2", a: 0},
{o: 160.213, f: 166.987, l: "5", a: 1},
{o: 166.987, f: 179.027, l: "2", a: 0},
{o: 179.027, f: 185.853, l: "1", a: 1},
{o: 185.853, f: 193.8, l: "6", a: 0},
{o: 193.8, f: 215.573, l: "1", a: 1},
{o: 215.573, f: 232.04, l: "3", a: 0},
{o: 232.04, f: 238.2, l: "8", a: 1}],
[{o: 0, f: 36.505, l: "a", a: 0},
{o: 36.505, f: 70.03, l: "a", a: 1},
{o: 70.03, f: 96.85, l: "b", a: 0},
{o: 96.85, f: 137.08, l: "a", a: 1},
{o: 137.08, f: 164.645, l: "b", a: 0},
{o: 164.645, f: 204.875, l: "a", a: 1},
{o: 204.875, f: 219.03, l: "b", a: 0},
{o: 219.03, f: 237.655, l: "c", a: 1}],
[{o: 0, f: 16.08, l: "n1", a: 0},
{o: 16.08, f: 42.701, l: "A", a: 1},
{o: 42.701, f: 48.982, l: "n2", a: 0},
{o: 48.982, f: 75.906, l: "A", a: 1},
{o: 75.906, f: 104.803, l: "B", a: 0},
{o: 104.803, f: 114.73, l: "n3", a: 1},
{o: 114.73, f: 142.617, l: "A", a: 0},
{o: 142.617, f: 171.921, l: "B", a: 1},
{o: 171.921, f: 182.462, l: "n5", a: 0},
{o: 182.462, f: 209.978, l: "A", a: 1},
{o: 209.978, f: 238.19, l: "n6", a: 0}],
[{o: 0, f: 0.08, l: "J", a: 0},
{o: 0.08, f: 11.6, l: "E", a: 1},
{o: 11.6, f: 15.408, l: "I", a: 0},
{o: 15.408, f: 53.228, l: "E", a: 1},
{o: 53.228, f: 56.704, l: "B", a: 0},
{o: 56.704, f: 60.672, l: "E", a: 1},
{o: 60.672, f: 63.94, l: "B", a: 0},
{o: 63.94, f: 69.336, l: "A", a: 1},
{o: 69.336, f: 73.376, l: "I", a: 0},
{o: 73.376, f: 96.908, l: "E", a: 1},
{o: 96.908, f: 114.76, l: "F", a: 0},
{o: 114.76, f: 129.344, l: "E", a: 1},
{o: 129.344, f: 135.664, l: "A", a: 0},
{o: 135.664, f: 139.924, l: "I", a: 1},
{o: 139.924, f: 144.372, l: "E", a: 0},
{o: 144.372, f: 181.904, l: "B", a: 1},
{o: 181.904, f: 186.888, l: "E", a: 0},
{o: 186.888, f: 190.38, l: "B", a: 1},
{o: 190.38, f: 197.716, l: "E", a: 0},
{o: 197.716, f: 203.012, l: "A", a: 1},
{o: 203.012, f: 207.344, l: "I", a: 0},
{o: 207.344, f: 211.436, l: "E", a: 1},
{o: 211.436, f: 230.284, l: "B", a: 0},
{o: 230.284, f: 235.888, l: "D", a: 1},
{o: 235.888, f: 238.29, l: "J", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000148.ogg";

var artist = "Henry Mancini";

var track = "Dreamsville";
