import sys
import os
import re
import matplotlib.pyplot as plt
from datetime import datetime
from collections import defaultdict
import matplotlib.font_manager as fm
import calendar
import io
from PIL import Image
from typing import List

# 이미지 저장 디렉토리 설정 (Spring Boot의 resources/static 디렉토리)
#IMAGE_DIR = '/ec2에서 복사할 스프링부트 경로/src/main/resources/static/'
IMAGE_DIR = 'C:\Users\Master\TalkSsogi_Workspace/' #테스트용

def save_image(output_file: str, image: Image.Image):
    file_path = os.path.join(IMAGE_DIR, output_file)
    image.save(file_path)

# 한글 폰트 설정
def set_korean_font():
    path ='C:/Users/Master/AppData/Local/Microsoft/Windows/Fonts/D2Coding-Ver1.3.2-20180524-all.ttc'  # 내 노트북에 설치된 한글 폰트 경로
    font_name = fm.FontProperties(fname=path, size=10).get_name()
    plt.rc('font', family=font_name)

set_korean_font()

# 언제부터 언제까지 누구의 일별 보낸 메시지 수를 나타낸 그래프
def create_message_count_graph(file_path, start_date, end_date, user_name=None):
    date_format = "%Y-%m-%d"
    start_date = datetime.strptime(start_date, date_format)
    end_date = datetime.strptime(end_date, date_format)
    
    dates = []
    counts = []

    with open(file_path, 'r', encoding='utf-8') as file:
        for line in file:
            date_str, count = line.strip().split(":")
            date = datetime.strptime(date_str, date_format)
            # 해당 범위 내의 날짜에 대한 메시지 수만 리스트에 추출
            if start_date <= date <= end_date:
                dates.append(date)
                counts.append(int(count))

    plt.figure(figsize=(10, 5))
    plt.plot(dates, counts, marker='o')
    plt.title(f"{'전체' if user_name is None else user_name}의 메시지 수")
    plt.xlabel("날짜")
    plt.ylabel("메시지 수")
    plt.grid(True)
    plt.xticks(rotation=45)
    plt.tight_layout()

    output_file = f"{'group' if user_name is None else user_name}_message_count_graph.png"
    buf = io.BytesIO()
    plt.savefig(buf, format='png', bbox_inches='tight')
    buf.seek(0)
    plt.close()
    
    image = Image.open(buf)
    save_image(output_file, image)

    return output_file

# 활발한 시간대 그래프
def create_hourly_message_graph(file_path, start_date, end_date, user_name=None):
    date_format = "%Y-%m-%d"
    start_date = datetime.strptime(start_date, date_format)
    end_date = datetime.strptime(end_date, date_format)
    
    hourly_counts = defaultdict(int)

    with open(file_path, 'r', encoding='utf-8') as file:
        for line in file:
            date_str, hours_str = line.strip().split("(")
            date = datetime.strptime(date_str, date_format)
            if start_date <= date <= end_date:
                hours_str = hours_str.rstrip(")")
                hour_counts = [hour.split(":") for hour in hours_str.split(",")]
                for hour, count in hour_counts:
                    hourly_counts[hour] += int(count)

    hours = list(hourly_counts.keys())
    counts = list(hourly_counts.values())

    plt.figure(figsize=(10, 5))
    plt.bar(hours, counts)
    plt.title(f"{'Group' if user_name is None else user_name}의 시간대별 메시지 수")
    plt.xlabel("시간대")
    plt.ylabel("메시지 수")
    plt.grid(True)
    plt.xticks(rotation=45)
    plt.tight_layout()

    output_file = f"{'group' if user_name is None else user_name}_hourly_message_activity_graph.png"
    buf = io.BytesIO()
    plt.savefig(buf, format='png', bbox_inches='tight')
    buf.seek(0)
    plt.close()
    
    image = Image.open(buf)
    save_image(output_file, image)

    return output_file

# 대화를 보내지 않은 날짜를 담은 달력 그래프를 위한 메서드들
# 달력 만들 범위의 달로 구성된 리스트 만들기
def get_months_range(start_date: datetime, end_date: datetime) -> List[int]:
    months = []
    
    # 초기 월과 연도
    current_month = start_date.month
    current_year = start_date.year
    
    # 마지막 월과 연도
    end_month = end_date.month
    end_year = end_date.year
    
    while current_year < end_year or (current_year == end_year and current_month <= end_month):
        months.append((current_year, current_month))
        # 다음 월로 이동
        if current_month == 12:
            current_month = 1
            current_year += 1
        else:
            current_month += 1
    
    return months

# 0인 날짜는 빨간색으로 달력 이미지 만들기
def plot_calendar(year, month, special_dates, start_date, end_date):
    # 달력 데이터 가져오기
    cal = calendar.monthcalendar(year, month)

    # 그림의 크기 설정 (너비, 높이) 400dp=2.5인치
    fig, ax = plt.subplots(figsize=(4.8, 3.5))  # 인치

    # 제목 설정 (연도-월)
    ax.set_title(f'{year}-{month:02d}')  # 월을 두 자리로 포맷

    # 테이블 그리기
    cell_colors = [['white']*7 for _ in range(len(cal))]  # 모든 셀을 기본 색상으로 설정
    cell_texts = [['' for _ in range(7)] for _ in range(len(cal))]  # 모든 셀을 빈 문자열로 초기화

    # 특정 날짜의 셀 색상 변경 및 텍스트 설정
    for row in range(len(cal)):
        for col in range(7):
            day = cal[row][col]
            if day > 0:
                date = datetime(year, month, day)
                if start_date <= date <= end_date:
                    cell_texts[row][col] = str(day)
                    if date in special_dates:
                        cell_colors[row][col] = 'lightcoral'  # count가 0인 날짜의 배경색을 lightcoral로 설정
                    else:
                        cell_colors[row][col] = 'white'  # count가 1 이상인 날짜의 배경색을 white로 설정
                else:
                    cell_texts[row][col] = ''  # 범위 외의 날짜는 빈 문자열로 설정

    table = ax.table(cellText=cell_texts, loc='center', cellLoc='center',
                     colLabels=['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
                     colColours=['lightgray']*7,
                     cellColours=cell_colors,
                     bbox=[0.1, 0.3, 0.8, 0.6])

    ax.axis('off')  # 축 제거

    # 이미지를 메모리에 저장
    buf = io.BytesIO()
    plt.savefig(buf, format='png', bbox_inches='tight')
    buf.seek(0)
    plt.close()
    
    return Image.open(buf)

def create_combined_calendar_image(file_path, start_date, end_date):
    date_format = "%Y-%m-%d"
    start_date = datetime.strptime(start_date, date_format)
    end_date = datetime.strptime(end_date, date_format)

    # 파일에서 데이터 읽기
    date_message_counts = {}
    with open(file_path, 'r', encoding='utf-8') as file:
        for line in file:
            date_str, count_str = line.strip().split(':')
            date = datetime.strptime(date_str, "%Y-%m-%d")
            count = int(count_str)
            date_message_counts[date] = count

    # 0인 날짜 찾기
    special_dates = set(date for date, count in date_message_counts.items() if count == 0)

    months = get_months_range(start_date, end_date)

    images = []
    for year, month in months:
        img = plot_calendar(year, month, special_dates, start_date, end_date)
        images.append(img)

    # 이미지 크기 계산
    widths, heights = zip(*(i.size for i in images))

    total_width = sum(widths)
    max_height = max(heights)

    # 빈 이미지 생성
    combined_image = Image.new('RGB', (total_width, max_height))

    # 이미지 붙이기
    x_offset = 0
    for img in images:
        combined_image.paste(img, (x_offset, 0))
        x_offset += img.width

    output_file = "combined_calendar.png"
    save_image(output_file, combined_image)

    return output_file  # 생성된 파일 경로 반환

if __name__ == "__main__":
    if len(sys.argv) != 6:
        print("Usage: python basic-python.py startDate endDate searchWho resultsItem filePath")
        sys.exit(1)
    
    start_date = sys.argv[1]
    end_date = sys.argv[2]
    search_who = sys.argv[3]
    results_item = sys.argv[4]
    file_path = sys.argv[5]

    if results_item == "보낸 메시지 수 그래프":
        output_file = create_message_count_graph(file_path, start_date, end_date, search_who)
        print(f"{output_file}")
    elif results_item == "활발한 시간대 그래프":
        output_file = create_hourly_message_graph(file_path, start_date, end_date, search_who)
        print(f"{output_file}")
    elif results_item == "대화를 보내지 않은 날짜":
        output_file = create_combined_calendar_image(file_path, start_date, end_date)
        print(f"{output_file}")
    else:
        print("Unknown resultsItem:", results_item)
        sys.exit(1)
