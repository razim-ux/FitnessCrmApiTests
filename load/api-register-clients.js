import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 5,
    iterations: 5,
};

const BASE_URL = 'https://coach-platform-nine.vercel.app';

export default function () {
    const uniqueEmail = `load_user_${__VU}_${Date.now()}@test.com`;
    const password = 'qwerty321';

    const registerRes = http.post(
        `${BASE_URL}/api/auth/register`,
        JSON.stringify({
            email: uniqueEmail,
            password: password,
        }),
        {
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
            },
        }
    );

    const sessionCookie = registerRes.cookies.session?.[0]?.value;

    check(registerRes, {
        'register status is 200': (res) => res.status === 200,
        'register has session cookie': () => Boolean(sessionCookie),
    });

    const clientsRes = http.get(`${BASE_URL}/api/clients`, {
        headers: {
            'Accept': 'application/json',
            'Cookie': `session=${sessionCookie}`,
        },
    });

    check(clientsRes, {
        'clients status is 200': (res) => res.status === 200,
    });

    sleep(1);
}