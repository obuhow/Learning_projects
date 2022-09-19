from urllib.request import urlopen
from bs4 import BeautifulSoup

html = urlopen("https://stepik.org/media/attachments/lesson/209719/2.html").read().decode('utf-8')
s = str(html)
list = []

a = 0
b = 0
while a != -1:
    a = s.find('<code>', b)
    b = s.find('</code>', a + 1)
    list.append(s[a+6:b])

list.sort()
i = -1
counter = 1
often_words = []
max = 1

for c in list:
    if list[i] == list[i+1]:
        counter += 1
    if list[i] != list[i+1] and counter > 1:
        if counter >= max:
            max = counter
        counter = 1
    i += 1
i = -1
counter = 1
for c in list:
    if list[i] == list[i+1]:
        counter += 1
    if list[i] != list[i+1] and counter > 1:
        if counter == max:
            often_words.append(list[i])
        counter = 1
    i += 1

a = 0
while a < len(often_words):
    print(often_words[a], end=' ')
    a += 1



