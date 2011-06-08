var data = [
[{o: 0.078, f: 6.776, l: "A", a: 0},
{o: 6.776, f: 13.392, l: "A", a: 1},
{o: 13.392, f: 19.975, l: "E", a: 0},
{o: 19.975, f: 26.615, l: "E", a: 1},
{o: 26.615, f: 33.159, l: "A", a: 0},
{o: 33.159, f: 39.831, l: "A", a: 1},
{o: 39.831, f: 46.431, l: "E", a: 0},
{o: 46.431, f: 53.088, l: "E", a: 1},
{o: 53.088, f: 59.729, l: "G", a: 0},
{o: 59.729, f: 66.321, l: "G", a: 1},
{o: 66.321, f: 72.962, l: "G", a: 0},
{o: 72.962, f: 79.536, l: "G", a: 1},
{o: 79.536, f: 86.135, l: "A", a: 0},
{o: 86.135, f: 92.757, l: "A", a: 1},
{o: 92.757, f: 99.399, l: "E", a: 0},
{o: 99.399, f: 106.058, l: "E", a: 1},
{o: 106.058, f: 112.687, l: "G", a: 0},
{o: 112.687, f: 119.295, l: "G", a: 1},
{o: 119.295, f: 125.915, l: "G", a: 0},
{o: 125.915, f: 132.558, l: "G", a: 1},
{o: 132.558, f: 139.108, l: "A", a: 0},
{o: 139.108, f: 145.76, l: "A", a: 1},
{o: 145.76, f: 152.353, l: "E", a: 0},
{o: 152.353, f: 158.999, l: "E", a: 1},
{o: 158.999, f: 165.617, l: "E", a: 0},
{o: 165.617, f: 172.288, l: "E", a: 1},
{o: 172.288, f: 178.852, l: "E", a: 0},
{o: 178.852, f: 194.022, l: "E", a: 1}],
[{o: 0.004, f: 12.44, l: "A", a: 0},
{o: 12.44, f: 22.364, l: "B", a: 1},
{o: 22.364, f: 39.308, l: "D", a: 0},
{o: 39.308, f: 53.248, l: "C", a: 1},
{o: 53.248, f: 59.444, l: "D", a: 0},
{o: 59.444, f: 66.204, l: "C", a: 1},
{o: 66.204, f: 77.796, l: "C", a: 0},
{o: 77.796, f: 85.532, l: "D", a: 1},
{o: 85.532, f: 92.548, l: "D", a: 0},
{o: 92.548, f: 106.208, l: "C", a: 1},
{o: 106.208, f: 112, l: "D", a: 0},
{o: 112, f: 118.76, l: "C", a: 1},
{o: 118.76, f: 130.76, l: "C", a: 0},
{o: 130.76, f: 138.212, l: "D", a: 1},
{o: 138.212, f: 148.548, l: "D", a: 0},
{o: 148.548, f: 164.692, l: "C", a: 1},
{o: 164.692, f: 182.484, l: "C", a: 0},
{o: 182.484, f: 189.632, l: "E", a: 1}],
[{o: 0.004, f: 12.44, l: "E", a: 0},
{o: 12.44, f: 22.364, l: "F", a: 1},
{o: 22.364, f: 39.308, l: "G", a: 0},
{o: 39.308, f: 53.248, l: "C", a: 1},
{o: 53.248, f: 59.444, l: "D", a: 0},
{o: 59.444, f: 66.204, l: "C", a: 1},
{o: 66.204, f: 77.796, l: "C", a: 0},
{o: 77.796, f: 85.532, l: "B", a: 1},
{o: 85.532, f: 92.548, l: "B", a: 0},
{o: 92.548, f: 106.208, l: "A", a: 1},
{o: 106.208, f: 112, l: "D", a: 0},
{o: 112, f: 118.76, l: "C", a: 1},
{o: 118.76, f: 130.76, l: "C", a: 0},
{o: 130.76, f: 138.212, l: "H", a: 1},
{o: 138.212, f: 148.548, l: "I", a: 0},
{o: 148.548, f: 164.692, l: "A", a: 1},
{o: 164.692, f: 182.484, l: "A", a: 0},
{o: 182.484, f: 189.632, l: "J", a: 1}],
[{o: 0.44, f: 25.893, l: "5", a: 0},
{o: 25.893, f: 39.947, l: "6", a: 1},
{o: 39.947, f: 52.213, l: "7", a: 0},
{o: 52.213, f: 76.773, l: "1", a: 1},
{o: 76.773, f: 86.693, l: "4", a: 0},
{o: 86.693, f: 92.92, l: "3", a: 1},
{o: 92.92, f: 105.253, l: "2", a: 0},
{o: 105.253, f: 129.733, l: "1", a: 1},
{o: 129.733, f: 139.667, l: "4", a: 0},
{o: 139.667, f: 154.147, l: "3", a: 1},
{o: 154.147, f: 187.253, l: "2", a: 0},
{o: 187.253, f: 192.173, l: "8", a: 1}],
[{o: 0, f: 25.33, l: "a", a: 0},
{o: 25.33, f: 39.485, l: "b", a: 1},
{o: 39.485, f: 57.365, l: "b", a: 0},
{o: 57.365, f: 79.715, l: "c", a: 1},
{o: 79.715, f: 97.595, l: "b", a: 0},
{o: 97.595, f: 110.26, l: "b", a: 1},
{o: 110.26, f: 159.43, l: "c", a: 0},
{o: 159.43, f: 173.585, l: "b", a: 1},
{o: 173.585, f: 193.7, l: "b", a: 0}],
[{o: 0, f: 26.122, l: "n1", a: 0},
{o: 26.122, f: 45.987, l: "A", a: 1},
{o: 45.987, f: 54.648, l: "n2", a: 0},
{o: 54.648, f: 79.064, l: "A", a: 1},
{o: 79.064, f: 98.94, l: "A", a: 0},
{o: 98.94, f: 105.14, l: "n4", a: 1},
{o: 105.14, f: 125.005, l: "A", a: 0},
{o: 125.005, f: 132.029, l: "n5", a: 1},
{o: 132.029, f: 151.893, l: "A", a: 0},
{o: 151.893, f: 158.523, l: "n6", a: 1},
{o: 158.523, f: 178.387, l: "A", a: 0},
{o: 178.387, f: 193.98, l: "n7", a: 1}],
[{o: 0, f: 0.004, l: "E", a: 0},
{o: 0.004, f: 13.132, l: "C", a: 1},
{o: 13.132, f: 26.348, l: "A", a: 0},
{o: 26.348, f: 39.588, l: "C", a: 1},
{o: 39.588, f: 52.692, l: "A", a: 0},
{o: 52.692, f: 79.308, l: "B", a: 1},
{o: 79.308, f: 92.548, l: "C", a: 0},
{o: 92.548, f: 105.788, l: "A", a: 1},
{o: 105.788, f: 132.26, l: "B", a: 0},
{o: 132.26, f: 145.512, l: "C", a: 1},
{o: 145.512, f: 181.916, l: "A", a: 0},
{o: 181.916, f: 189.536, l: "D", a: 1},
{o: 189.536, f: 193.96, l: "E", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000776.ogg";

var artist = "RWC MDB G 2001 M07";

var track = "5";
