import sys
import re
import os
from collections import defaultdict
from basicAnalysis import ChatRoom

# 터미널 출력 인코딩 설정
sys.stdout.reconfigure(encoding='utf-8')

def main():
    if len(sys.argv) < 3:
        print("Usage: python caller_prediction.py <file_path> <keyword>")
        sys.exit(1)

    file_path = sys.argv[1]
    keyword = sys.argv[2]
    chat_room = ChatRoom(file_path)
    chat_room.preprocess_and_analyze()

    # 사용자별 텍스트 파일 경로
    members = chat_room.get_members()
    base_dir = os.path.dirname(file_path)  # 파일 경로의 디렉토리 부분만 추출
    member_file_paths = {member: chat_room.get_personal_file_path(member) for member in members}

    # 예측한 발신자 결과
    caller_prediction = ""

    def safe_read_file(file_path):
        try:
            with open(file_path, 'r', encoding='utf-8') as file:
                return file.read()
        except Exception as e:
            print(f"Error reading file {file_path}: {e}")
            return ""

    # 키워드 빈도 계산 함수
    def calculate_keyword_frequency(text, keyword):
        return len(re.findall(re.escape(keyword), text))

    # 키워드 빈도를 저장할 딕셔너리
    keyword_frequency = defaultdict(int)

    # 각 사용자별 메시지를 읽고 키워드 빈도를 계산
    for member, path in member_file_paths.items():
        text = safe_read_file(path)
        keyword_frequency[member] = calculate_keyword_frequency(text, keyword)

    # 가장 높은 빈도를 가진 사용자 찾기
    if keyword_frequency:
        caller_prediction = max(keyword_frequency, key=keyword_frequency.get)

    # 예측 결과 출력
    print(caller_prediction)

if __name__ == "__main__":
    main()
