'use client'

import { useRouter } from 'next/navigation';

export default function HomePage() {
	const router = useRouter();

	return <>
		<div style={{ padding: '40px', textAlign: 'center' }}>
			<h1>홈페이지</h1>
			<div style={{ display: 'flex', gap: '10px', justifyContent: 'center', marginTop: '20px' }}>
				<button 
					onClick={() => router.push('/auth/login')}
					style={{ padding: '10px 20px', cursor: 'pointer' }}
				>
					로그인
				</button>
				
				<button 
					onClick={() => router.push('/auth/register')}
					style={{ padding: '10px 20px', cursor: 'pointer' }}
				>
					회원가입
				</button>
				
				<button 
					// onClick={() => handleLogout()}
					style={{ padding: '10px 20px', cursor: 'pointer' }}
				>
					로그아웃
				</button>
				
				<button 
					onClick={() => router.push('/qna')}
					style={{ padding: '10px 20px', cursor: 'pointer' }}
				>
					QnA
				</button>
			</div>
		</div>
	</>
}