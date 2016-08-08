import os
import re


def disk_stat():
    disk_infos = os.popen('df -hP | grep -v tmpfs | grep -v Filesystem').read().split('\n')
    disk_infos.remove('')
    return disk_infos


def map_str_array(str):
    return re.split('\s+', str)


def gen_disk_array():
    str_arr = disk_stat()
    return map(map_str_array, str_arr)


def println(f):
    line = 0
    f.write('<p>Disk Space</p>\n')
    f.write('<p>\n')
    f.write('<table border="1">\n')
    f.write(
        '<tr><th class="awrbg">Filesystem</th><th class="awrbg">Size</th><th class="awrbg">Used</th><th class="awrbg">Avail</th><th class="awrbg">Use%</th><th class="awrbg">Mounted on</th><tr>\n')
    for (fs, tsize, usize, asize, uu, mo) in gen_disk_array():
        if line % 2 == 0:
            f.write(
                '<tr><td>' + fs + '</td><td>' + tsize + '</td><td>' + usize + '</td><td>' + asize + '</td><td>' + uu + '</td><td>' + mo + '</td></tr>\n')
        else:
            f.write(
                '<tr><td class="awrcgk">' + fs + '</td><td class="awrcgk">' + tsize + '</td><td class="awrcgk">' + usize + '</td><td class="awrcgk">' + asize + '</td><td class="awrcgk">' + uu + '</td><td class="awrcgk">' + mo + '</td></tr>\n')
        line += 1
    f.write('</table>\n')
    f.write('</p>\n')
