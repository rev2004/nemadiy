var data = [
[{o: 0.372, f: 25.263, l: "A", a: 0},
{o: 25.263, f: 39.938, l: "B", a: 1},
{o: 39.938, f: 69.403, l: "C", a: 0},
{o: 69.403, f: 76.871, l: "E", a: 1},
{o: 76.871, f: 84.191, l: "B", a: 0},
{o: 84.191, f: 113.451, l: "C", a: 1},
{o: 113.451, f: 124.328, l: "E", a: 0},
{o: 124.328, f: 154.169, l: "D", a: 1},
{o: 154.169, f: 183.748, l: "C", a: 0},
{o: 183.748, f: 196.438, l: "E", a: 1},
{o: 196.438, f: 217.348, l: "B'", a: 0}],
[{o: 0.076, f: 13.884, l: "A", a: 0},
{o: 13.884, f: 28.148, l: "A", a: 1},
{o: 28.148, f: 39.816, l: "A", a: 0},
{o: 39.816, f: 46.044, l: "A", a: 1},
{o: 46.044, f: 56.908, l: "A", a: 0},
{o: 56.908, f: 68.212, l: "A", a: 1},
{o: 68.212, f: 75.8, l: "A", a: 0},
{o: 75.8, f: 81.268, l: "B", a: 1},
{o: 81.268, f: 90.104, l: "A", a: 0},
{o: 90.104, f: 101.332, l: "A", a: 1},
{o: 101.332, f: 111.812, l: "A", a: 0},
{o: 111.812, f: 119.748, l: "A", a: 1},
{o: 119.748, f: 129.948, l: "A", a: 0},
{o: 129.948, f: 137.808, l: "A", a: 1},
{o: 137.808, f: 148.252, l: "A", a: 0},
{o: 148.252, f: 154.628, l: "A", a: 1},
{o: 154.628, f: 160.676, l: "A", a: 0},
{o: 160.676, f: 172.232, l: "A", a: 1},
{o: 172.232, f: 179.58, l: "A", a: 0},
{o: 179.58, f: 185.408, l: "A", a: 1},
{o: 185.408, f: 195.36, l: "A", a: 0},
{o: 195.36, f: 204.452, l: "A", a: 1},
{o: 204.452, f: 211.248, l: "A", a: 0}],
[{o: 0.076, f: 13.884, l: "A", a: 0},
{o: 13.884, f: 28.148, l: "A", a: 1},
{o: 28.148, f: 39.816, l: "B", a: 0},
{o: 39.816, f: 46.044, l: "C", a: 1},
{o: 46.044, f: 56.908, l: "C", a: 0},
{o: 56.908, f: 68.212, l: "C", a: 1},
{o: 68.212, f: 75.8, l: "C", a: 0},
{o: 75.8, f: 81.268, l: "E", a: 1},
{o: 81.268, f: 90.104, l: "C", a: 0},
{o: 90.104, f: 101.332, l: "C", a: 1},
{o: 101.332, f: 111.812, l: "C", a: 0},
{o: 111.812, f: 119.748, l: "C", a: 1},
{o: 119.748, f: 129.948, l: "F", a: 0},
{o: 129.948, f: 137.808, l: "D", a: 1},
{o: 137.808, f: 148.252, l: "D", a: 0},
{o: 148.252, f: 154.628, l: "G", a: 1},
{o: 154.628, f: 160.676, l: "C", a: 0},
{o: 160.676, f: 172.232, l: "C", a: 1},
{o: 172.232, f: 179.58, l: "H", a: 0},
{o: 179.58, f: 185.408, l: "I", a: 1},
{o: 185.408, f: 195.36, l: "A", a: 0},
{o: 195.36, f: 204.452, l: "J", a: 1},
{o: 204.452, f: 211.248, l: "K", a: 0}],
[{o: 0.387, f: 7.04, l: "6", a: 0},
{o: 7.04, f: 16.96, l: "5", a: 1},
{o: 16.96, f: 24.293, l: "7", a: 0},
{o: 24.293, f: 47.4, l: "2", a: 1},
{o: 47.4, f: 79.653, l: "1", a: 0},
{o: 79.653, f: 91.6, l: "2", a: 1},
{o: 91.6, f: 123.653, l: "1", a: 0},
{o: 123.653, f: 143.053, l: "3", a: 1},
{o: 143.053, f: 154.32, l: "1", a: 0},
{o: 154.32, f: 161.4, l: "2", a: 1},
{o: 161.4, f: 189.907, l: "1", a: 0},
{o: 189.907, f: 196.52, l: "8", a: 1},
{o: 196.52, f: 207.307, l: "2", a: 0},
{o: 207.307, f: 217.187, l: "4", a: 1}],
[{o: 0, f: 32.035, l: "a", a: 0},
{o: 32.035, f: 76.735, l: "b", a: 1},
{o: 76.735, f: 120.69, l: "b", a: 0},
{o: 120.69, f: 148.255, l: "c", a: 1},
{o: 148.255, f: 188.485, l: "b", a: 0},
{o: 188.485, f: 216.795, l: "d", a: 1}],
[{o: 0, f: 41.749, l: "n1", a: 0},
{o: 41.749, f: 66.026, l: "A", a: 1},
{o: 66.026, f: 76.661, l: "B", a: 0},
{o: 76.661, f: 86.32, l: "n3", a: 1},
{o: 86.32, f: 109.227, l: "A", a: 0},
{o: 109.227, f: 154.169, l: "n4", a: 1},
{o: 154.169, f: 180.222, l: "A", a: 0},
{o: 180.222, f: 192.226, l: "B", a: 1},
{o: 192.226, f: 217.014, l: "n5", a: 0}],
[{o: 0, f: 0.076, l: "I", a: 0},
{o: 0.076, f: 26.368, l: "H", a: 1},
{o: 26.368, f: 49.2, l: "G", a: 0},
{o: 49.2, f: 70.144, l: "A", a: 1},
{o: 70.144, f: 93.188, l: "G", a: 0},
{o: 93.188, f: 114.128, l: "A", a: 1},
{o: 114.128, f: 142.332, l: "F", a: 0},
{o: 142.332, f: 162.912, l: "D", a: 1},
{o: 162.912, f: 184.104, l: "A", a: 0},
{o: 184.104, f: 211.12, l: "C", a: 1},
{o: 211.12, f: 217.315, l: "I", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami001317.ogg";

var artist = "Michael Jackson";

var track = "she s out of my life";
