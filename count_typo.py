import sys
import re
import json
import os
import io
import time
from collections import defaultdict
from basicAnalysis import ChatRoom
from pykospacing import Spacing
from concurrent.futures import ProcessPoolExecutor

# Spacing 인스턴스 생성
spacing = Spacing()

def preprocess_message(message):
    message = re.sub(r'[^\w\s]', '', message)
    return message

def count_typos_in_message_batch(messages):
    typo_count = 0
    for message in messages:
        message = preprocess_message(message)
        if len(message) > 1000:
            message = message[:1000]
        try:
            # PyKoSpacing을 사용하여 띄어쓰기 및 맞춤법 교정
            corrected_message = spacing(message)
            # 교정된 메시지와 원본 메시지를 비교하여 오타 수 계산
            typos = sum(1 for a, b in zip(message, corrected_message) if a != b)
            typo_count += typos
        except Exception as e:
            print(f"Error checking spelling for message: {message}\nException: {e}")
            continue
    return typo_count

def batch_process_messages(messages, batch_size):
    """메시지를 배치로 나누어 처리하는 함수"""
    total_typo_count = 0
    for i in range(0, len(messages), batch_size):
        batch = messages[i:i + batch_size]
        total_typo_count += count_typos_in_message_batch(batch)
    return total_typo_count

def count_typo_for_member(messages, batch_size=20):
    """한 멤버의 메시지를 배치 처리하는 함수"""
    return batch_process_messages(messages, batch_size)

def main():
    start_time = time.time()  # 전체 시작 시간 기록

    if len(sys.argv) < 2:
        print("Usage: python main_script.py <file_path>")
        sys.exit(1)

    sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf8')

    file_path = sys.argv[1]
    chat_room = ChatRoom(file_path)
    chat_room.preprocess_and_analyze()
    chat_room.save_daily_message_count()
    chat_room.save_daily_hourly_message_count()

    print(chat_room.room_name)
    print(','.join(chat_room.get_members()))
    print(chat_room.get_headcount())

    ranking_results_map = {}

    def safe_read_file(file_path):
        try:
            with open(file_path, 'r', encoding='utf-8') as file:
                return file.read()
        except Exception as e:
            print(f"Error reading file {file_path}: {e}")
            return ""
    
    def count_typo(chat_room, batch_size=40):
        typo_count = {user_name: 0 for user_name in chat_room.get_members()}

        members = chat_room.get_members()
        with ProcessPoolExecutor() as executor:
            futures = {}
            for member in members:
                personal_file_path = chat_room.get_personal_file_path(member)
                if personal_file_path:
                    with open(personal_file_path, 'r', encoding='utf-8') as file:
                        messages = file.read().split('\n')
                        futures[member] = executor.submit(count_typo_for_member, messages, batch_size)
            
            for member, future in futures.items():
                typo_count[member] = future.result()

        sorted_typo_count = sorted(typo_count.items(), key=lambda item: item[1], reverse=True)
        return sorted_typo_count

    typo_count_ranking = count_typo(chat_room)
    ranking_results_map['오타'] = {user_name: count for user_name, count in typo_count_ranking}

    typo_output_file = 'C:/TalkSsogi_Workspace/TalkSsogi/typo_ranking_result.json'
    with open(typo_output_file, 'w', encoding='utf-8') as f:
            json.dump(ranking_results_map, f, ensure_ascii=False, indent=4)
    
    # 종료 시간 기록 및 소요 시간 계산
    end_time = time.time()
    elapsed_time = end_time - start_time
    print(f"Total time taken: {elapsed_time:.2f} seconds")

if __name__ == "__main__":
    main()