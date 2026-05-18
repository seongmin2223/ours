import { useEffect, useState } from 'react'

interface User {
  id: number;
  email: string;
  gender: string;
  role: string;
  provider: string;
}

function App() {
  const [users, setUsers] = useState<User[]>([]);

  useEffect(() => {
    fetch('http://localhost:8080/api/mock/users')
        .then(response => response.json())
        .then(data => setUsers(data))
        .catch(error => console.error('데이터 로딩 실패:', error));
  }, []);

  return (
      <div style={{ padding: '20px', fontFamily: 'sans-serif' }}>
        <h1>ours 커뮤니티 회원 목록 (React)</h1>
        <table border={1} style={{ width: '100%', borderCollapse: 'collapse', marginTop: '20px' }}>
          <thead>
          <tr style={{ backgroundColor: '#f2f2f2' }}>
            <th>ID</th>
            <th>이메일</th>
            <th>성별</th>
            <th>권한</th>
            <th>가입 경로</th>
          </tr>
          </thead>
          <tbody>
          {users.length === 0 ? (
              <tr>
                <td colSpan={5} style={{ textAlign: 'center', padding: '10px' }}>가입된 유저가 없습니다. 포스트맨으로 등록해 보세요!</td>
              </tr>
          ) : (
              users.map(user => (
                  <tr key={user.id} style={{ textAlign: 'center' }}>
                    <td style={{ padding: '10px' }}>{user.id}</td>
                    <td>{user.email}</td>
                    <td>{user.gender}</td>
                    <td>{user.role}</td>
                    <td>{user.provider}</td>
                  </tr>
              ))
          )}
          </tbody>
        </table>
      </div>
  )
}

export default App