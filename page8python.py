import sys
from collections import defaultdict
import re


#메시지 수가 가장 많았던 날
def find_date_with_max_count(file_path):
    max_count = -1
    max_date = None

    try:
        with open(file_path, 'r', encoding='utf-8') as file:
            for line in file:
                date, count = line.strip().split(':')
                count = int(count)
                if count > max_count:
                    max_count = count
                    max_date = date
    except Exception as e:
        #print(f"Error reading file {file_path}: {e}")
        return None

    return max_date, max_count

#대화를 하지 않은 날
def find_dates_with_zero_count(file_path):
    zero_count_dates = []

    try:
        with open(file_path, 'r', encoding='utf-8') as file:
            for line in file:
                date, count = line.strip().split(':')
                count = int(count)
                if count == 0:
                    zero_count_dates.append(date)
    except Exception as e:
        #print(f"Error reading file {file_path}: {e}")
        return None

    #zero_count_dates.append("2024-07-05")
    #zero_count_dates.append("2024-08-05")

    return ', '.join(zero_count_dates)

#평균적으로 가장 활발한 시간대
def find_max_message_time_slot(file_path):
    # 시간대별 메시지 수를 저장할 딕셔너리
    time_slot_counts = defaultdict(int)

    # 파일 읽기
    with open(file_path, 'r', encoding='utf-8') as file:
        lines = file.readlines()

    # 정규 표현식 패턴 정의
    pattern = re.compile(r'(\d{4}-\d{2}-\d{2})\((.*?)\)')

    for line in lines:
        line = line.strip()
        match = pattern.match(line)
        if not match:
            continue

        _, time_data = match.groups()

        # 시간대별 데이터 파싱
        for entry in time_data.split(','):
            time_range, count = entry.split(':')
            count = int(count)

            # 시간대에 메시지 수 추가
            time_slot_counts[time_range] += count

    # 가장 많은 메시지 수를 가진 시간대 찾기
    max_time_slot = max(time_slot_counts, key=time_slot_counts.get)
    max_count = time_slot_counts[max_time_slot]

    return max_time_slot, max_count


def main():
    if len(sys.argv) < 3:
        print("Usage: python main_script.py <daily_file_path> <hourly_file_path>")
        sys.exit(1)

    daily_file_path = sys.argv[1]
    hourly_file_path = sys.argv[2]

    max_date, max_count = find_date_with_max_count(daily_file_path)
    if max_date and max_count is not None:
        print(f"{max_date}({max_count}건)")

    zero_count_dates = find_dates_with_zero_count(daily_file_path)
    if zero_count_dates:
        print(f"{zero_count_dates}")

    max_time_slot, max_count = find_max_message_time_slot(hourly_file_path)
    if max_time_slot and max_count is not None:
        print(f"{max_time_slot}({max_count}건)")

if __name__ == "__main__":
    main()
