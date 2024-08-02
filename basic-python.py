import sys
import re
import json
import os
from collections import defaultdict
from basicAnalysis import ChatRoom

def main():
    if len(sys.argv) < 2:
        print("Usage: python main_script.py <file_path>")
        sys.exit(1)

    file_path = sys.argv[1]
    chat_room = ChatRoom(file_path)
    chat_room.preprocess_and_analyze()

    print(chat_room.room_name)
    print(','.join(chat_room.get_members()))
    print(chat_room.get_headcount())

    # 랭킹을 저장할 딕셔너리
    ranking_results_map = {}

    def safe_read_file(file_path):
        try:
            with open(file_path, 'r', encoding='utf-8') as file:
                return file.read()
        except Exception as e:
            print(f"Error reading file {file_path}: {e}")
            return ""

    # ----------------메시지 수 기준 랭킹 출력----------------
    message_count_ranking = chat_room.rank_users_by_message_count()
    ranking_results_map['메시지'] = {user_name: f"{count}개" for user_name, count in message_count_ranking}

    # ----------------오타 수 기준 랭킹 출력----------------
    typo_count_ranking = chat_room.rank_users_by_typo_count()
    ranking_results_map['오타'] = {user_name: str(count) for user_name, count in typo_count_ranking}

    # ----------------초성 사용 횟수 기준 랭킹 출력----------------
    initial_message_count_ranking = chat_room.rank_users_by_initial_message_count()
    ranking_results_map['초성'] = {user_name: str(count) for user_name, count in initial_message_count_ranking}

    # ----------------언급----------------
    def count_mentions(chat_room):
        mention_counts = {user_name: 0 for user_name in chat_room.get_members()}
        mentioned_counts = {user_name: 0 for user_name in chat_room.get_members()}

        for member in chat_room.get_members():
            personal_file_path = chat_room.get_personal_file_path(member)
            if personal_file_path:
                messages = safe_read_file(personal_file_path).split(';')
                for message in messages:
                    mentions = re.findall(r'@(\w+)', message)
                    for mentioned_name in mentions:
                        mentioned_name = mentioned_name.strip()
                        if mentioned_name in mention_counts:
                            mention_counts[member] += 1
                            mentioned_counts[mentioned_name] += 1

        mention_counts = dict(sorted(mention_counts.items(), key=lambda item: item[1], reverse=True))
        mentioned_counts = dict(sorted(mentioned_counts.items(), key=lambda item: item[1], reverse=True))

        return mention_counts, mentioned_counts

    mention, mentioned = count_mentions(chat_room)
    ranking_results_map['언급한'] = {user_name: str(count) for user_name, count in mention.items()}
    ranking_results_map['언급된'] = {user_name: str(count) for user_name, count in mentioned.items()}

    # ----------------사진 보낸 개수----------------
    def count_photos(chat_room):
        photo_count = {user_name: 0 for user_name in chat_room.get_members()}

        for member in chat_room.get_members():
            personal_file_path = chat_room.get_personal_file_path(member)
            if personal_file_path:
                messages = safe_read_file(personal_file_path).split(';')
                for message in messages:
                    message = message.strip()
                    matches = re.findall(r'사진\s*(\d*)\s*장?', message)
                    for match in matches:
                        if re.fullmatch(r'사진\s*\d*\s*장?', message):
                            if match.isdigit():
                                photo_count[member] += int(match)
                            else:
                                photo_count[member] += 1
        photo_count = dict(sorted(photo_count.items(), key=lambda item: item[1], reverse=True))
        return photo_count

    photo = count_photos(chat_room)
    ranking_results_map['사진'] = {user_name: str(count) for user_name, count in photo.items()}

    # ----------------이모티콘 보낸 개수----------------
    def count_emojis(chat_room):
        emoji_count = {user_name: 0 for user_name in chat_room.get_members()}

        for member in chat_room.get_members():
            personal_file_path = chat_room.get_personal_file_path(member)
            if personal_file_path:
                messages = safe_read_file(personal_file_path).split(';')
                for message in messages:
                    message = message.strip()
                    if message == '이모티콘':
                        emoji_count[member] += 1

        basic_emoji_counts = chat_room.rank_users_by_emoji_count()
        for name, count in basic_emoji_counts:
            if name in emoji_count:
                emoji_count[name] += count
        emoji_count = dict(sorted(emoji_count.items(), key=lambda item: item[1], reverse=True))
        return emoji_count

    emoji = count_emojis(chat_room)
    ranking_results_map['이모티콘'] = {user_name: str(count) for user_name, count in emoji.items()}

    # ----------------검색어 받아서 검색하는 함수 만들기----------------
    def search_keyword_in_personal_files(chat_room, keyword):
        members = chat_room.get_members()
        search_results = defaultdict(int)

        for user_name in members:
            personal_file_path = chat_room.get_personal_file_path(user_name)
            if not os.path.exists(personal_file_path):
                continue

            messages = safe_read_file(personal_file_path).split(';')
            for message in messages:
                if keyword.strip() in message:
                    search_results[user_name] += 1

        sorted_results = dict(sorted(search_results.items(), key=lambda item: item[1], reverse=True))
        return sorted_results

    keyword = '안뇽'
    results = search_keyword_in_personal_files(chat_room, keyword)
    ranking_results_map['검색어'] = {user_name: str(count) for user_name, count in results.items()}

    # ----------------메시지 삭제 횟수----------------
    def deleted_message_results(chat_room):
        delete_count = {user_name: 0 for user_name in chat_room.get_members()}

        for member in chat_room.get_members():
            personal_file_path = chat_room.get_personal_file_path(member)
            if personal_file_path:
                messages = safe_read_file(personal_file_path).split(';')
                for message in messages:
                    message = message.strip()
                    if '삭제된 메시지입니다.' in message:
                        delete_count[member] += 1
        delete_count = dict(sorted(delete_count.items(), key=lambda item: item[1], reverse=True))
        return delete_count

    deleted_message_results = deleted_message_results(chat_room)
    ranking_results_map['삭제'] = {user_name: str(count) for user_name, count in deleted_message_results.items()}

    # ----------------메시지 평균 길이----------------
    def calculate_average_message_length(chat_room):
        user_average_lengths = {}
        for user_name in chat_room.get_members():
            personal_file_path = chat_room.get_personal_file_path(user_name)
            if os.path.exists(personal_file_path):
                content = safe_read_file(personal_file_path).split(';')

                total_length = 0
                num_messages = 0
                for message in content:
                    message = message.strip()
                    if message:
                        total_length += len(message)
                        num_messages += 1

                average_length = total_length / num_messages if num_messages > 0 else 0
                user_average_lengths[user_name] = int(average_length)

        sorted_averages = dict(sorted(user_average_lengths.items(), key=lambda item: item[1], reverse=True))
        return sorted_averages

    average_length = calculate_average_message_length(chat_room)
    ranking_results_map['평균 길이'] = {user_name: str(avg_length) for user_name, avg_length in average_length.items()}

    # 결과를 JSON으로 변환하여 파일로 저장
    output_file = 'C:/Talkssogi_Workspace/TalkSsogi/ranking_results.json'
    with open(output_file, 'w', encoding='utf-8') as f:
        json.dump(ranking_results_map, f, ensure_ascii=False, indent=4)

    print(f"\n전체 랭킹 결과가 '{output_file}' 파일로 저장되었습니다.")

if __name__ == "__main__":
    main()
