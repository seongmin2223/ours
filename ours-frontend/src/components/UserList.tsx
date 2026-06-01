import { useEffect, useState } from 'react';
import { jwtDecode } from 'jwt-decode';

interface User {
    id: number;
    email: string;
    gender: string;
    role: string;
    provider: string;
    nickname: string;
}

interface JwtPayload {
    name: string;
    email: string;
}

const UserList = () => {
    const [users, setUsers] = useState<User[]>([]);
    const [userName, setUserName] = useState<string>('');

    useEffect(() => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            try {
                const decoded = jwtDecode<JwtPayload>(token);
                setUserName(decoded.name || '사용자');
            } catch (e) {
                console.error("토큰 디코딩 실패", e);
            }
        }

        fetch('http://localhost:8080/api/mock/users')
            .then(response => response.json())
            .then(data => setUsers(data))
            .catch(error => console.error('데이터 로딩 실패:', error));
    }, []);

    return (
        <div style={{ padding: '20px', fontFamily: 'sans-serif' }}>
            <div style={{ marginBottom: '20px', padding: '15px', backgroundColor: '#f0f0f0', borderRadius: '8px' }}>
                {userName ? <strong>환영합니다, {userName}님</strong> : "로그인이 필요합니다."}
            </div>

            <h1>ours 커뮤니티 회원 목록</h1>
            <table border={1} style={{ width: '100%', borderCollapse: 'collapse', marginTop: '20px' }}>
                <thead>
                <tr style={{ backgroundColor: '#f2f2f2' }}>
                    <th>ID</th>
                    <th>이름</th>
                    <th>이메일</th>
                    <th>성별</th>
                    <th>권한</th>
                    <th>가입 경로</th>
                </tr>
                </thead>
                <tbody>
                {users.map(user => (
                    <tr key={user.id} style={{ textAlign: 'center' }}>
                        <td>{user.id}</td>
                        <td>{user.nickname || '-'}</td>
                        <td>{user.email}</td>
                        <td>{user.gender}</td>
                        <td>{user.role}</td>
                        <td>{user.provider}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default UserList;