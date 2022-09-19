from urllib.request import urlopen
from bs4 import BeautifulSoup

html = urlopen("https://stepik.org/media/attachments/lesson/209723/5.html").read().decode('utf-8')
s = str(html)
soup = BeautifulSoup(s, 'html.parser')

sum = 0
for tr in soup.find_all('tr'):
    for td in tr.find_all('td'):
        sum += int(td.string)

print(sum)


