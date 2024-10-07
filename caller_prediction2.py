import sys
import numpy as np
import pickle
import pandas as pd
from tensorflow.keras.models import load_model
from tensorflow.keras.preprocessing.sequence import pad_sequences
import os
import logging
import warnings

# TensorFlow와 관련된 불필요한 로그 출력을 줄이기
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'  # 경고 및 정보 메시지 무시
os.environ['TF_ENABLE_ONEDNN_OPTS'] = '0'  # oneDNN 최적화 비활성화

# TensorFlow 로그 무시
logging.getLogger('tensorflow').setLevel(logging.ERROR)

# InconsistentVersionWarning 경고 무시
warnings.filterwarnings("ignore", category=UserWarning, module='sklearn')

# 저장된 토크나이저 로드
with open('C:/TalkSsogi_Workspace/TalkSsogi/tokenizer.pickle', 'rb') as handle:
    tokenizer = pickle.load(handle)

# 저장된 모델 로드
model = load_model('C:/TalkSsogi_Workspace/TalkSsogi/third_model.keras')

# 발신자 예측 함수
def predict_sender(message):
    max_len = 60  # 이전에 설정한 시퀀스 최대 길이와 동일해야 함

    # 입력된 메시지를 토큰화
    sequence = tokenizer.texts_to_sequences([message])
    data_padded = pad_sequences(sequence, maxlen=max_len)

    # 모델로 예측 수행
    prediction = model.predict(data_padded, verbose=0)  # verbose=0으로 불필요한 출력 제거

    # 가장 높은 확률을 가진 클래스 선택
    predicted_sender_index = np.argmax(prediction)

    # 인코딩된 레이블을 다시 원래 발신자로 변환
    with open('C:/TalkSsogi_Workspace/TalkSsogi/label_encoder.pickle', 'rb') as handle:
        label_encoder = pickle.load(handle)

    predicted_sender = label_encoder.inverse_transform([predicted_sender_index])

    return predicted_sender[0]

# 예측된 발신자가 포함된 행의 번호를 찾는 함수
def find_sender_row(predicted_sender):
    # CSV 파일 불러오기
    df = pd.read_csv('C:/TalkSsogi_Workspace/TalkSsogi/sender_embeddings_lighter.csv')

    # 'Sender' 열에서 predicted_sender와 일치하는 행의 인덱스 찾기
    matching_rows = df.index[df['Sender'] == predicted_sender].tolist()

    if len(matching_rows) > 0:
        return matching_rows[0]  # 첫 번째 매칭 행 반환
    else:
        return None  # 해당 발신자가 없으면 None 반환

# 명령행에서 입력받은 메시지로 발신자 예측
if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("사용법: python caller_prediction.py '메시지 내용'")
        sys.exit()

    # 명령행에서 메시지 입력 받기
    new_message = sys.argv[1]

    # 발신자 예측
    predicted_sender = predict_sender(new_message)

    # sender_embeddings_lighter.csv에서 해당 발신자의 행 번호 찾기
    row_number = find_sender_row(predicted_sender)

    # 예측된 발신자와 행 번호 출력
    if row_number is not None:
        if row_number > 0:
            print(f"{row_number - 1}")
        else:
            print(f"{row_number}")
    else:
        print(f"'{predicted_sender}' 발신자를 찾을 수 없습니다.")
