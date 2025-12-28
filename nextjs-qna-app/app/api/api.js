const getBaseURL = () => {
    if (typeof window !== 'undefined') {
        return `${window.location.protocol}//${window.location.hostname}:8080/api`;
    }
    return 'http://back:8080/api';
};

const API_BASE_URL = getBaseURL();

export async function baseRequest(
    url,
    method,
    headers,
    body,
    errMsg
) {
    const res = await fetch(`${API_BASE_URL}${url}`,{
        method: method,
        headers: headers,
        body: body
    });

    if (!res.ok)
        throw new Error(errMsg);

    console.log(`⭐️ 결과: ${res}`)
    return res.json();
}
