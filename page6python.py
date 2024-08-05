import os
import sys
from collections import Counter
from wordcloud import WordCloud
import matplotlib.pyplot as plt
from PIL import Image

# 이미지 저장 디렉토리 설정 (테스트용)
IMAGE_DIR = 'C:/Users/Master/TalkSsogi_Workspace/'

def save_image(output_file: str, image: Image.Image):
    file_path = os.path.join(IMAGE_DIR, output_file)
    image.save(file_path)

def generate_wordcloud_from_file(personal_file_path):
    if not os.path.exists(personal_file_path):
        print(f"파일이 존재하지 않습니다: {personal_file_path}")
        return

    excluded_words = {'안', '아'}

    # 개인 파일에서 형태소 분석 결과 읽기
    with open(personal_file_path, 'r', encoding='utf-8') as file:
        content = file.read()

    # 형태소 분석 결과를 분리
    morphs = content.split("\n")

    # 제외할 단어를 걸러낸 형태소 리스트
    morphs = [word for word in morphs if word not in excluded_words and word.strip() != '']

    # 형태소 빈도 계산
    word_counts = Counter(morphs)
    #print(f"형태소 빈도 수: {word_counts}")  # 디버깅용 출력

    # 최소 빈도수 이상인 단어들만 필터링
    min_frequency = 2
    filtered_word_counts = {word: count for word, count in word_counts.items() if count >= min_frequency}
    #print(f"필터링된 형태소 빈도 수: {filtered_word_counts}")  # 디버깅용 출력

    if not filtered_word_counts:
        print("빈도수가 기준에 맞는 단어가 없습니다.")
        return

    # 워드 클라우드 생성
    wordcloud = WordCloud(
        font_path='C:/Users/Master/AppData/Local/Microsoft/Windows/Fonts/D2Coding-Ver1.3.2-20180524-all.ttc',  # 한글 폰트 경로
        background_color='white',
        width=800,
        height=600
    ).generate_from_frequencies(filtered_word_counts)

    # 워드 클라우드 이미지를 메모리로 저장
    buf = io.BytesIO()
    plt.figure(figsize=(10, 8))
    plt.imshow(wordcloud, interpolation='bilinear')
    plt.axis('off')
    plt.title("워드 클라우드")
    plt.tight_layout()
    plt.savefig(buf, format='png')
    buf.seek(0)
    plt.close()

    # 메모리에서 이미지 열기
    image = Image.open(buf)

    # 개인 파일 경로에서 crnum과 searchWho 추출
    file_name = os.path.basename(personal_file_path)  # 'group.txt' 또는 'userId_personal.txt'
    crnum = file_name.split('/')[0]
    searchWho = 'group' if 'group.txt' in file_name else file_name.split('_')[0]
    output_file = f"{searchWho}_wordcloud_image.png"

    save_image(output_file, image)
    print(f"{output_file}")

def main():
    if len(sys.argv) != 2:
        print("Usage: python script.py <personal_file_path>")
        sys.exit(1)

    personal_file_path = sys.argv[1]
    generate_wordcloud_from_file(personal_file_path)

if __name__ == "__main__":
    main()
