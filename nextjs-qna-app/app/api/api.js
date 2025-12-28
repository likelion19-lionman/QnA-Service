const getBaseURL = () => {
    if (typeof window !== 'undefined') {
        return `${window.location.protocol}//${window.location.hostname}:8080/api`;
    }
    return 'http://back:8080/api';
};

const API_BASE_URL = getBaseURL();

export async function baseRequest(url, method, headers, body, errMsg) {
    const fetchOptions = {
        method: method,
        headers: headers,
        body: JSON.stringify(body),
        credentials: 'include'
    };

    let res = await fetch(`${API_BASE_URL}${url}`, fetchOptions);

    // 인증 예외 코드 401
    if (res.status === 401) {
        console.log('액세스 토큰이 만료되어 다시 갱신합니다...');
        await refresh();

        console.log('토큰 갱신 성공. 이전 요청 다시 진행...');
        res = await fetch(`${API_BASE_URL}${url}`, fetchOptions);
    }

    // 만약 재발급 후에도 ok 가 아니라면 에러 발생
    if (!res.ok)
        throw new Error(errMsg || '요청 실패');

    const data = await res.json();
    console.log('⭐️ 결과:', data); // 템플릿 리터럴 대신 , 를 써야 객체 내용이 보임
    return data;
}