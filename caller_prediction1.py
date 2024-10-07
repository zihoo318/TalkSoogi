import torch
import numpy as np
import pandas as pd
from transformers import BertTokenizer, BertModel
import torch.nn as nn
import sys

# BERT 모델과 토크나이저 초기화
tokenizer = BertTokenizer.from_pretrained('bert-base-uncased')
bert_model = BertModel.from_pretrained('bert-base-uncased')

class MLP(nn.Module):
    def __init__(self, input_dim, output_dim):
        super(MLP, self).__init__()
        # MLP 모델의 레이어 정의
        self.fc1 = nn.Linear(input_dim, 128)  # 입력 차원에서 128 차원으로 변환
        self.dropout1 = nn.Dropout(0.2)       # 드롭아웃 비율 20%
        self.fc2 = nn.Linear(128, 64)         # 128 차원에서 64 차원으로 변환
        self.dropout2 = nn.Dropout(0.2)       # 드롭아웃 비율 20%
        self.fc3 = nn.Linear(64, output_dim)  # 64 차원에서 출력 차원으로 변환

    def forward(self, x):
        # 순전파 함수 정의
        x = torch.relu(self.fc1(x))  # 첫 번째 레이어를 통과하고 ReLU 활성화 함수 적용
        x = self.dropout1(x)  # 첫 번째 레이어 후 드롭아웃 적용
        x = torch.relu(self.fc2(x))  # 두 번째 레이어를 통과하고 ReLU 활성화 함수 적용
        x = self.dropout2(x)  # 두 번째 레이어 후 드롭아웃 적용
        x = self.fc3(x)  # 마지막 레이어를 통과하여 최종 출력 생성
        return x

# 모델과 손실 함수 및 옵티마이저 초기화
input_dim = 48  # 입력 차원 (문장 임베딩 차원)
output_dim = 48  # 출력 차원 (임베딩 차원)
model = MLP(input_dim, output_dim)

# 저장된 모델 불러오기
model_path = 'C:\TalkSsogi_Workspace\TalkSsogi\mlp_model.pth'  # 불러올 모델 파일 경로
model.load_state_dict(torch.load(model_path))
model.eval()  # 모델을 평가 모드로 설정

def sentence_to_embedding(sentence):
    # 문장을 BERT 토크나이저로 토큰화
    tokens = tokenizer(sentence, return_tensors='pt', padding=True, truncation=True)
    with torch.no_grad():  # 그래디언트 계산 비활성화
        output = bert_model(**tokens)
    # BERT의 마지막 층에서 평균을 취해 임베딩 벡터 생성
    embedding = output.last_hidden_state.mean(dim=1).squeeze(0)
    # 768차원 벡터를 48차원 벡터로 변환
    reshaped_embedding = embedding.view(-1, 16)  # 768차원을 16씩 나누어 48개의 16차원 벡터 생성
    reduced_embedding = reshaped_embedding.mean(dim=1)  # 각 16차원 벡터의 평균을 취해 48차원 벡터 생성
    return reduced_embedding

def predict_sender_embedding(sentences):
    embeddings = []
    for sentence in sentences:
        embedding = sentence_to_embedding(sentence)
        embeddings.append(embedding)
    
    with torch.no_grad():
        embeddings_tensor = torch.stack(embeddings)
        predictions = model(embeddings_tensor)
        avg_prediction = predictions.mean(dim=0).numpy()
    return avg_prediction

def load_csv_embeddings():
    # CSV 파일에서 임베딩 벡터를 로드
    df = pd.read_csv("C:\TalkSsogi_Workspace\TalkSsogi\sender_embeddings_lighter.csv")
    embeddings = df['Embedding'].apply(lambda x: np.fromstring(x, sep=','))
    return np.array(list(embeddings))

def find_most_similar(csv_embeddings, predicted_embeddings):
    # 코사인 유사도를 계산해 각 csv_embedding에 대해 predicted_embeddings와 비교
    similarities = np.dot(csv_embeddings, predicted_embeddings.T) / (
        np.linalg.norm(csv_embeddings, axis=1)[:, None] * np.linalg.norm(predicted_embeddings, axis=1))
    
    # 각 csv_embedding에 대해 가장 유사한 predicted_embedding의 인덱스를 찾음
    most_similar_indices = np.argmax(similarities, axis=1)

    similarities = np.dot(csv_embeddings, predicted_embeddings.T) / (
    np.linalg.norm(csv_embeddings, axis=1)[:, None] * np.linalg.norm(predicted_embeddings, axis=1))

    return most_similar_indices


def main():
    # sys.argv[1:]로 첫 번째 인자부터 끝까지 받아 한 개의 문자열로 합침
    input_data = ' '.join(sys.argv[1:])  # 발신자와 메시지 데이터를 포함한 전체 문자열
    
    # 발신자별로 데이터를 구분
    sender_data = input_data.split('<@@@@@')
    sender_data = [data.split('@@@@@>') for data in sender_data if '@@@@@>' in data]
    
    sender_names = [data[0].strip() for data in sender_data]  # 발신자 이름 추출
    sender_messages_list = [data[1].strip().split('@@@>') for data in sender_data]  # 메시지 추출
    
    # CSV 파일에서 임베딩 벡터 로드
    csv_embeddings = load_csv_embeddings()

    # 각 발신자의 임베딩 벡터 예측
    predicted_embeddings = []
    for sender_messages in sender_messages_list:
        predicted_embedding = predict_sender_embedding(sender_messages)
        predicted_embeddings.append(predicted_embedding)
    
    predicted_embeddings = np.array(predicted_embeddings)

    # 새로 입력된 발신자 임베딩 벡터와 CSV 임베딩 벡터 비교
    most_similar_indices = find_most_similar(csv_embeddings, predicted_embeddings)

    # 결과를 새로 입력된 발신자 이름 순서대로 매핑
    most_similar_names = [sender_names[i] for i in most_similar_indices]

    # 결과 출력 (새로 입력된 발신자 이름 순서대로 가장 유사한 발신자 이름 출력)
    result = ','.join(most_similar_names)
    print("다음 줄부터 result출력")
    print(result)

if __name__ == "__main__":
    main()