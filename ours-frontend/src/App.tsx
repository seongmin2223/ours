import { BrowserRouter, Routes, Route } from 'react-router-dom';
import UserList from './components/UserList';
import OAuth2RedirectHandler from './components/OAuth2RedirectHandler';

function App() {
  return (
      <BrowserRouter>
        <Routes>
          {/* 메인 화면 */}
          <Route path="/" element={<UserList />} />
          {/* 카카오 로그인 후 돌아오는 곳 */}
          <Route path="/oauth/redirect" element={<OAuth2RedirectHandler />} />
        </Routes>
      </BrowserRouter>
  );
}

export default App;