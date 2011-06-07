var data = [
[{o: 0.046, f: 0.987, l: "Z", a: 0},
{o: 0.987, f: 12.74, l: "A", a: 1},
{o: 12.74, f: 37.076, l: "A", a: 0},
{o: 37.076, f: 80.506, l: "B", a: 1},
{o: 80.506, f: 104.648, l: "A", a: 0},
{o: 104.648, f: 160.39, l: "B", a: 1},
{o: 160.39, f: 183.692, l: "A", a: 0},
{o: 183.692, f: 252.391, l: "B", a: 1},
{o: 252.391, f: 269.793, l: "Z", a: 0}],
[{o: 0.18, f: 14.424, l: "D", a: 0},
{o: 14.424, f: 30.72, l: "C", a: 1},
{o: 30.72, f: 43.612, l: "B", a: 0},
{o: 43.612, f: 69.252, l: "B", a: 1},
{o: 69.252, f: 81.052, l: "D", a: 0},
{o: 81.052, f: 98.284, l: "C", a: 1},
{o: 98.284, f: 117.232, l: "B", a: 0},
{o: 117.232, f: 135.4, l: "A", a: 1},
{o: 135.4, f: 153.116, l: "E", a: 0},
{o: 153.116, f: 162.184, l: "F", a: 1},
{o: 162.184, f: 170.708, l: "G", a: 0},
{o: 170.708, f: 183.552, l: "D", a: 1},
{o: 183.552, f: 203.048, l: "A", a: 0},
{o: 203.048, f: 222.288, l: "A", a: 1},
{o: 222.288, f: 238.732, l: "A", a: 0},
{o: 238.732, f: 252.188, l: "D", a: 1},
{o: 252.188, f: 269.652, l: "H", a: 0}],
[{o: 0.18, f: 14.424, l: "D", a: 0},
{o: 14.424, f: 30.72, l: "C", a: 1},
{o: 30.72, f: 43.612, l: "B", a: 0},
{o: 43.612, f: 69.252, l: "B", a: 1},
{o: 69.252, f: 81.052, l: "E", a: 0},
{o: 81.052, f: 98.284, l: "C", a: 1},
{o: 98.284, f: 117.232, l: "B", a: 0},
{o: 117.232, f: 135.4, l: "A", a: 1},
{o: 135.4, f: 153.116, l: "F", a: 0},
{o: 153.116, f: 162.184, l: "G", a: 1},
{o: 162.184, f: 170.708, l: "H", a: 0},
{o: 170.708, f: 183.552, l: "I", a: 1},
{o: 183.552, f: 203.048, l: "A", a: 0},
{o: 203.048, f: 222.288, l: "A", a: 1},
{o: 222.288, f: 238.732, l: "A", a: 0},
{o: 238.732, f: 252.188, l: "J", a: 1},
{o: 252.188, f: 269.652, l: "K", a: 0}],
[{o: 0.52, f: 13.533, l: "5", a: 0},
{o: 13.533, f: 30.173, l: "3", a: 1},
{o: 30.173, f: 36.04, l: "4", a: 0},
{o: 36.04, f: 54.333, l: "1", a: 1},
{o: 54.333, f: 61.027, l: "2", a: 0},
{o: 61.027, f: 70.44, l: "1", a: 1},
{o: 70.44, f: 80.52, l: "2", a: 0},
{o: 80.52, f: 95.027, l: "3", a: 1},
{o: 95.027, f: 103.267, l: "4", a: 0},
{o: 103.267, f: 122.76, l: "1", a: 1},
{o: 122.76, f: 128.333, l: "2", a: 0},
{o: 128.333, f: 147.293, l: "1", a: 1},
{o: 147.293, f: 160.173, l: "2", a: 0},
{o: 160.173, f: 180.333, l: "3", a: 1},
{o: 180.333, f: 200.253, l: "1", a: 0},
{o: 200.253, f: 206.893, l: "2", a: 1},
{o: 206.893, f: 223.213, l: "1", a: 0},
{o: 223.213, f: 230.227, l: "2", a: 1},
{o: 230.227, f: 251.92, l: "1", a: 0},
{o: 251.92, f: 256.573, l: "8", a: 1},
{o: 256.573, f: 263.307, l: "7", a: 0},
{o: 263.307, f: 269.587, l: "6", a: 1}],
[{o: 0, f: 46.935, l: "a", a: 0},
{o: 46.935, f: 72.265, l: "b", a: 1},
{o: 72.265, f: 119.2, l: "a", a: 0},
{o: 119.2, f: 140.06, l: "b", a: 1},
{o: 140.06, f: 192.955, l: "a", a: 0},
{o: 192.955, f: 216.05, l: "b", a: 1},
{o: 216.05, f: 239.145, l: "b", a: 0},
{o: 239.145, f: 269.69, l: "c", a: 1}],
[{o: 0, f: 2.612, l: "n1", a: 0},
{o: 2.612, f: 14.501, l: "B", a: 1},
{o: 14.501, f: 19.04, l: "n2", a: 0},
{o: 19.04, f: 70.542, l: "A", a: 1},
{o: 70.542, f: 87.144, l: "B", a: 0},
{o: 87.144, f: 135.895, l: "A", a: 1},
{o: 135.895, f: 144.01, l: "n5", a: 0},
{o: 144.01, f: 156.177, l: "B", a: 1},
{o: 156.177, f: 166.731, l: "n6", a: 0},
{o: 166.731, f: 214.239, l: "A", a: 1},
{o: 214.239, f: 239.874, l: "n7", a: 0},
{o: 239.874, f: 251.995, l: "B", a: 1},
{o: 251.995, f: 269.398, l: "n8", a: 0}],
[{o: 0, f: 0.18, l: "H", a: 0},
{o: 0.18, f: 0.18, l: "B", a: 1},
{o: 0.18, f: 24.152, l: "A", a: 0},
{o: 24.152, f: 45.888, l: "G", a: 1},
{o: 45.888, f: 69.252, l: "F", a: 0},
{o: 69.252, f: 91.444, l: "C", a: 1},
{o: 91.444, f: 115.26, l: "G", a: 0},
{o: 115.26, f: 148.632, l: "F", a: 1},
{o: 148.632, f: 171.072, l: "C", a: 0},
{o: 171.072, f: 193.992, l: "G", a: 1},
{o: 193.992, f: 232.244, l: "F", a: 0},
{o: 232.244, f: 247.664, l: "D", a: 1},
{o: 247.664, f: 269.652, l: "B", a: 0},
{o: 269.652, f: 269.723, l: "H", a: 1}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000151.ogg";
