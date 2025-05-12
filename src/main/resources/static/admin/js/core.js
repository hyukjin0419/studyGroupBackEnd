/**
 * 관리자 페이지 코어 기능
 * 공통 유틸리티, 인증, 페이지 라우팅 등을 처리합니다.
 */

// 전역 변수 및 초기화
const API_BASE_URL = '/api/admin';
let currentUser = null;
let csrfToken = null;

// 페이지 초기화
document.addEventListener('DOMContentLoaded', async () => {
    try {
        // CSRF 토큰 가져오기
        await getCsrfToken();
        
        // 사용자 정보 가져오기
        await getCurrentUser();

        // 네비게이션 이벤트 설정
        setupNavigation();

        // 로그아웃 이벤트 설정
        setupLogout();

        // 기본 페이지 로드 (대시보드)
        loadDashboard();
    } catch (error) {
        console.error('초기화 중 오류:', error);
        if (error.response && (error.response.status === 401 || error.response.status === 403)) {
            // 인증 오류 시 로그인 페이지로 리디렉션
            window.location.href = '/login';
        }
    }
});

/**
 * CSRF 토큰을 가져오고 저장합니다.
 */
async function getCsrfToken() {
    try {
        const response = await axios.get(`${API_BASE_URL}/get-csrf`);
        csrfToken = response.data;
        
        // 로그아웃 폼에 CSRF 토큰 설정
        document.getElementById('csrf-token-logout').value = csrfToken.token;
        
        // Axios 기본 헤더에 CSRF 토큰 설정
        axios.defaults.headers.common[csrfToken.headerName] = csrfToken.token;
        
        return csrfToken;
    } catch (error) {
        console.error('CSRF 토큰 가져오기 실패:', error);
        throw error;
    }
}

/**
 * 현재 로그인한 관리자 정보를 가져옵니다.
 */
async function getCurrentUser() {
    try {
        const response = await axios.get(`${API_BASE_URL}/current-user`);
        if (response.data.success) {
            currentUser = {
                id: response.data.id,
                userName: response.data.userName,
                email: response.data.email,
                role: response.data.role
            };
            
            // 사용자 이름 표시
            document.getElementById('user-name').textContent = currentUser.userName;
            
            return currentUser;
        }
    } catch (error) {
        console.error('사용자 정보 가져오기 실패:', error);
        throw error;
    }
}

/**
 * 네비게이션 메뉴 이벤트를 설정합니다.
 */
function setupNavigation() {
    console.log('Setting up navigation...');
    const navLinks = {
        'nav-dashboard': { title: '대시보드', load: loadDashboard },
        'nav-members': { title: '사용자 관리', load: loadMembers },
        'nav-studies': { title: '스터디 관리', load: loadStudies },
        'nav-announcements': { title: '공지사항 관리', load: loadAnnouncements }
    };
    
    // 각 메뉴 항목에 이벤트 리스너 추가
    for (const [id, info] of Object.entries(navLinks)) {
        const element = document.getElementById(id);
        if (element) {
            console.log(`Adding event listener to menu item: ${id}`);
            element.addEventListener('click', (e) => {
                e.preventDefault();
                console.log(`Menu clicked: ${id}`);
                
                // 활성 메뉴 클래스 변경
                document.querySelectorAll('.nav-link').forEach(link => link.classList.remove('active'));
                element.classList.add('active');
                
                // 페이지 제목 변경
                document.getElementById('page-title').textContent = info.title;
                
                // 해당 기능 로드
                try {
                    info.load();
                } catch (error) {
                    console.error(`Error loading ${id}:`, error);
                    showModal('오류 발생', `페이지 로드 중 오류가 발생했습니다: ${error.message}`);
                }
            });
        } else {
            console.warn(`Menu element not found: ${id}`);
        }
    }
}

/**
 * 로그아웃 이벤트를 설정합니다.
 */
function setupLogout() {
    const logoutBtn = document.getElementById('logout-btn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', (e) => {
            e.preventDefault();
            document.getElementById('logout-form').submit();
        });
    }
}

/**
 * 알림 모달을 표시합니다.
 */
function showModal(title, message, confirmCallback = null, showCancelButton = true) {
    // 기존 백드롭 제거
    const existingBackdrops = document.querySelectorAll('.modal-backdrop');
    existingBackdrops.forEach(backdrop => backdrop.remove());
    document.body.classList.remove('modal-open');
    document.body.style.overflow = '';
    document.body.style.paddingRight = '';
    
    // 기존 모달 인스턴스 제거
    const modalEl = document.getElementById('commonModal');
    const existingModalInstance = bootstrap.Modal.getInstance(modalEl);
    if (existingModalInstance) {
        existingModalInstance.dispose();
    }
    
    // 모달 새로 설정
    const modal = new bootstrap.Modal(modalEl);
    document.getElementById('commonModalLabel').textContent = title;
    document.getElementById('modal-body-content').innerHTML = message;
    
    const confirmBtn = document.getElementById('modal-confirm-btn');
    
    // 취소 버튼 표시 여부
    document.querySelector('#commonModal .btn-secondary').style.display = showCancelButton ? 'block' : 'none';
    
    // 확인 버튼 이벤트 재설정
    confirmBtn.replaceWith(confirmBtn.cloneNode(true));
    const newConfirmBtn = document.getElementById('modal-confirm-btn');
    
    if (confirmCallback) {
        newConfirmBtn.addEventListener('click', () => {
            modal.hide();
            confirmCallback();
        });
    } else {
        newConfirmBtn.addEventListener('click', () => {
            modal.hide();
        });
    }
    
    // 모달 닫힐 때 백드롭 제거 이벤트 추가
    modalEl.addEventListener('hidden.bs.modal', function () {
        const backdrops = document.querySelectorAll('.modal-backdrop');
        backdrops.forEach(backdrop => backdrop.remove());
        document.body.classList.remove('modal-open');
        document.body.style.overflow = '';
        document.body.style.paddingRight = '';
        console.log('Modal hidden, backdrops removed');
    }, { once: true });
    
    modal.show();
}

/**
 * 대시보드 페이지를 로드합니다.
 */
async function loadDashboard() {
    console.log('Loading dashboard...');
    try {
        // 대시보드 컨텐츠 표시
        showContent('dashboard-content');
        
        // 기본값으로 초기화
        document.getElementById('total-users').textContent = '--';
        document.getElementById('total-studies').textContent = '--';
        document.getElementById('total-announcements').textContent = '--';
        
        // 사용자 수 조회
        try {
            const usersResponse = await axios.get(`${API_BASE_URL}/users`);
            if (usersResponse.data.success) {
                document.getElementById('total-users').textContent = usersResponse.data.data.length.toLocaleString();
            }
        } catch (userError) {
            console.error('사용자 통계 로드 오류:', userError);
        }
        
        // 스터디 수 조회
        try {
            const studiesResponse = await axios.get(`${API_BASE_URL}/groups`);
            if (studiesResponse.data.success) {
                document.getElementById('total-studies').textContent = studiesResponse.data.data.length.toLocaleString();
            }
        } catch (studyError) {
            console.error('스터디 통계 로드 오류:', studyError);
        }
        
        // 공지사항 수 조회
        try {
            const announcementsResponse = await axios.get(`${API_BASE_URL}/announcements`);
            if (announcementsResponse.data.success) {
                document.getElementById('total-announcements').textContent = announcementsResponse.data.data.length.toLocaleString();
            }
        } catch (announcementError) {
            console.error('공지사항 통계 로드 오류:', announcementError);
        }
        
    } catch (error) {
        console.error('대시보드 로드 중 오류:', error);
        showModal('오류 발생', '대시보드 통계를 불러오는 중 오류가 발생했습니다.');
    }
}

/**
 * 지정된 컨텐츠 영역을 표시하고 나머지는 숨깁니다.
 */
function showContent(contentId) {
    console.log(`Showing content area: ${contentId}`);
    // 모든 컨텐츠 영역 가져오기
    const contentAreas = document.querySelectorAll('#content-area > div');
    console.log(`Found ${contentAreas.length} content areas`);
    
    // 각 컨텐츠 영역 순회하며 숨기기
    contentAreas.forEach(area => {
        if (area.id === contentId) {
            area.classList.remove('hidden');
            console.log(`Showing ${area.id}`);
        } else {
            area.classList.add('hidden');
            console.log(`Hiding ${area.id}`);
        }
    });
    
    // 스크롤을 페이지 상단으로 이동
    window.scrollTo(0, 0);
}

// 다른 모듈에서 호출하는 함수들 export
window.adminCore = {
    API_BASE_URL,
    getCurrentUser: () => currentUser,
    getCsrfToken: () => csrfToken,
    showModal,
    showContent
};

// 페이지별 로드 함수

// 사용자 관리 페이지 로드
async function loadMembers() {
    console.log('Loading members page...');
    try {
        // 컨텐츠 영역 표시
        showContent('members-content');
        
        // 회원 모듈 사용 가능여부 확인
        if (window.memberModule && typeof window.memberModule.initMembers === 'function') {
            console.log('Initializing member module...');
            await window.memberModule.initMembers();
            console.log('Member module initialized successfully');
        } else {
            console.error('회원 관리 모듈을 찾을 수 없습니다.');
            document.getElementById('members-content').innerHTML = '<div class="alert alert-danger">회원 관리 기능을 로드할 수 없습니다.</div>';
        }
    } catch (error) {
        console.error('회원 관리 페이지 로드 오류:', error);
        document.getElementById('members-content').innerHTML = `<div class="alert alert-danger">오류 발생: ${error.message}</div>`;
    }
}

function loadStudies() {
    console.log('Loading studies content...');
    showContent('studies-content');
    if (typeof window.studyModule !== 'undefined' && window.studyModule.initStudies) {
        console.log('Initializing study module from navigation');
        window.studyModule.initStudies();
    } else {
        console.error('스터디 관리 모듈이 로드되지 않았습니다.');
    }
}

function loadAnnouncements() {
    console.log('Loading announcements content...');
    showContent('announcements-content');
    if (typeof window.announcementModule !== 'undefined' && window.announcementModule.init) {
        console.log('Initializing announcement module from navigation');
        window.announcementModule.init();
    } else {
        console.error('공지사항 관리 모듈이 로드되지 않았습니다.');
    }
}
