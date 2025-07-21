import random


def generate_dates(num_entries=500):
    with open('dates.tmp', 'w') as f:
        for i in range(num_entries):
            year = random.randint(1900, 2030)
            mon = random.randint(1, 12)
            day = random.randint(1, 28)

            hh = random.randint(1, 23)
            mm = random.randint(1, 59)
            ss = random.randint(1, 59)

            formatted_date = f'"[{day:02d}/{mon:02d}/{year}:{hh:02d}:{mm:02d}:{ss:02d} +1000]",\n'
            f.write(formatted_date)


if __name__ == '__main__':
    generate_dates()
