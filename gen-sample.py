import random
from datetime import datetime, timedelta


def generate_log_entries(num_entries=100, error_rate=0.05):
    # Базовый IP адрес
    base_ip = "192.168.32."

    # Базовая дата
    base_date = datetime(2017, 6, 14, 10, 0, 0)

    with open('access.log', 'w') as f:
        for i in range(num_entries):
            # Генерируем случайный IP в диапазоне
            ip = f"{base_ip}{random.randint(100, 255)}"

            # Генерируем временную метку
            if random.random() <= 0.005:
                base_date += timedelta(seconds=random.randint(1, 3))
            formatted_date = base_date.strftime("%d/%m/%Y:%H:%M:%S +1000")

            # Генерируем rid - случайное шестизначное hex число
            rid = hex(random.randint(0x1000000, 0xFFFFFFF))[2:]

            # Генерируем время ответа
            response_time = round(random.uniform(20.0, 50.0), 6)

            # Определяем статус код (вероятность получить 500)
            status_code = 500 if random.random() < error_rate else 200

            # Для 500 ошибок увеличиваем время ответа
            if status_code == 500:
                response_time = round(random.uniform(50.0, 90.0), 6)

            # Формируем строку лога
            log_entry = (
                f'{ip} - - [{formatted_date}] '
                f'"PUT /rest/v1.4/documents?zone=default&_rid={rid} HTTP/1.1" '
                f'{status_code} 2 {response_time} "-" "@list-item-updater" prio:0\n'
            )

            f.write(log_entry)


if __name__ == "__main__":
    # Генерируем млн записей с 5% вероятностью ошибок
    generate_log_entries(1_000_000, 0.05)
    print("Log file 'access.log' has been generated successfully!")
