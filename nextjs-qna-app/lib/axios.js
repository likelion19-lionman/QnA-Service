import axios from 'axios';

const instance = axios.create({
    baseURL: '/api',  // l ec2를 사용할 땐 ec2 public ip로 수정
    withCredentials: true,
}); // axios를 커스텀마이징할 때 쓰는 함수(create)

instance.interceptors.request.use(config => {
    const token = localStorage.getItem('accessToken');
    const noAuthUrls = ['/auth'];

    if(!noAuthUrls.some(url => config.url.startsWith(url))){
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
}, error => Promise.reject(error))

instance.interceptors.response.use(  //응답이 들어왔을 때 인터셉트
    res => res, //성공했을 때 그냥 반환 
    async error => {
        
        if (error.response.status === 401 && !originalRequest._retry){
            originalRequest._retry = ture;

            try{
            const refreshToken = localStorage.getItem('refreshToken');
            const res = await instance.post('/auth/refresh', {refreshToken});

            const newAccessToken = res.data;
            localStorage.setItem('accessToken', newAccessToken);

            originalRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;
            return instance(originalRequest);
            }catch (error){
                console.error('리프레시 실패: ', error)
            }
        }

        return Promise.reject(error);
    }
);

export default instance;