import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
    vus: 3,
    duration: '10s',
};

const BASE_URL = 'https://coach-platform-nine.vercel.app';

export default function () {
    http.get(BASE_URL);
    sleep(1);
}