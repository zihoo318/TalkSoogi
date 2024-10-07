import os
import sys
import io
import requests
from collections import Counter
from wordcloud import WordCloud
import matplotlib.pyplot as plt
from PIL import Image
import matplotlib.font_manager as fm
import emoji
import re

# 이미지 저장 디렉토리 설정 (테스트용)
IMAGE_DIR = 'C:/TalkSsogi_Workspace/TalkSsogi/'

# # ec2에서
# IMAGE_DIR = '/home/ec2-user/workspace/'

def save_image(output_file: str, image: Image.Image):
    file_path = os.path.join(IMAGE_DIR, output_file)
    try:
        image.save(file_path)
    except Exception as e:
        print(f"Error saving image: {e}")

# 한글 폰트 설정
def set_korean_font():
    path = r"C:/TalkSsogi_Font/NanumGothic.ttf"
    fm.fontManager.addfont(path)
    font_properties = fm.FontProperties(fname=path)
    plt.rcParams['font.family'] = font_properties.get_name()
    font_name = fm.FontProperties(fname=path, size=10).get_name()
    plt.rc('font', family=font_name)
    return path

korean_font_path = set_korean_font()

# 이모티콘 제거 함수
def remove_emoji(text):
    return emoji.replace_emoji(text, replace='')

def generate_wordcloud_from_url(url):
    response = requests.get(url)
    response.encoding = 'utf-8'
    if response.status_code != 200:
        print(f"파일을 가져오지 못했습니다: {url}")
        return

    excluded_words = {'안', '아', '이모티콘', '사진', '거', '다'}
    content = response.text
    morphs = content.split("\n")

    # 이모티콘 제거 및 특정 단어 필터링
    morphs = [remove_emoji(word.strip()) for word in morphs if word.strip() not in excluded_words and word.strip() != '']

    word_counts = Counter(morphs)
    min_frequency = 2
    filtered_word_counts = {word: count for word, count in word_counts.items() if count >= min_frequency}

    if not filtered_word_counts:
        print("빈도수가 기준에 맞는 단어가 없습니다.")
        return

    wordcloud = WordCloud(
        font_path=korean_font_path,
        background_color='white',
        width=800,
        height=600
    ).generate_from_frequencies(filtered_word_counts)

    buf = io.BytesIO()
    plt.figure(figsize=(10, 8))
    plt.imshow(wordcloud, interpolation='bilinear')
    plt.axis('off')
    plt.title("워드 클라우드")
    plt.tight_layout()
    plt.savefig(buf, format='png')
    buf.seek(0)
    plt.close()

    image = Image.open(buf)

    file_name = os.path.basename(url)
    if 'group' in file_name:
        crnum = file_name.split('_')[0]
        searchWho = 'group'
    elif 'personal' in file_name:
        crnum = file_name.split('_')[0]
        searchWho = file_name.split('_')[1]
    output_file = f"{crnum}_{searchWho}_wordcloud_image.png"

    save_image(output_file, image)
    print(f"{output_file}")

def main():
    if len(sys.argv) != 2:
        print("Usage: python script.py <url>")
        sys.exit(1)

    url = sys.argv[1]
    generate_wordcloud_from_url(url)

if __name__ == "__main__":
    main()
