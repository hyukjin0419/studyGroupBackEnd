/**
 * 관리자 페이지 메인 스크립트
 * 관리자 페이지 초기화 및 화면 로드 관리를 담당합니다.
 */

// 관리자 페이지 초기화
document.addEventListener('DOMContentLoaded', function() {
    console.log('Admin page script initialized');
    
    // 버전 확인 및 초기화 정보 로그
    console.info('Admin page version: 1.0.1');
    console.info('Last updated: 2025-05-10');
    
    // 모듈 초기화 실행
    setTimeout(() => {
        try {
            initializeModules();
            console.log('All modules initialized');
        } catch (error) {
            console.error('Error initializing modules:', error);
        }
    }, 300);
    
    // 시작 화면을 대시보드로 설정
    if (window.adminCore && typeof window.adminCore.loadDashboard === 'function') {
        console.log('Loading initial dashboard view...');
        // 대시보드 페이지 로드 지연 (화면이 모두 로드되도록 지연)
        setTimeout(() => {
            try {
                // 대시보드 페이지 로드
                window.adminCore.loadDashboard();
            } catch (error) {
                console.error('Error loading dashboard:', error);
            }
        }, 500);
    } else {
        console.error('Core module not loaded or loadDashboard function not available');
    }
});

// 모듈 초기화 함수
function initializeModules() {
    console.log('Initializing admin modules...');
    
    // 멤버 모듈 초기화
    if (window.membersModule && typeof window.membersModule.init === 'function') {
        console.log('Initializing members module');
        window.membersModule.init();
    }
    
    // 스터디 모듈 초기화
    if (window.studiesModule && typeof window.studiesModule.init === 'function') {
        console.log('Initializing studies module');
        window.studiesModule.init();
    }
    
    // 공지사항 모듈 초기화 (모듈화 구조)
    if (window.announcementModule && typeof window.announcementModule.init === 'function') {
        console.log('Initializing announcement module');
        window.announcementModule.init();
    }
}

// 버튼 클릭 이벤트 테스트 지원 함수
function testButtonClicks() {
    console.log('Testing button clicks...');
    
    // 사이드바 메뉴 버튼 테스트
    const navButtons = [
        { id: 'nav-dashboard', name: '대시보드' },
        { id: 'nav-members', name: '사용자 관리' },
        { id: 'nav-studies', name: '스터디 관리' },
        { id: 'nav-announcements', name: '공지사항 관리' }
    ];
    
    // 각 버튼 로그 출력
    navButtons.forEach(button => {
        const element = document.getElementById(button.id);
        if (element) {
            console.log(`Button '${button.name}' (${button.id}) found`);
        } else {
            console.error(`Button '${button.name}' (${button.id}) NOT found`);
        }
    });
}

// 500ms 후 버튼 테스트 실행
setTimeout(testButtonClicks, 1000);


/**
 * 스크립트를 동적으로 로드합니다 (더 이상 사용하지 않지만,구 버버전 호환성을 위해 유지)
 * @param {string} src - 스크립트 소스 URL
 * @param {Function} callback - 로드 완료 후 실행할 콜백 함수
 */
function loadScript(src, callback) {
    console.log(`[DEPRECATED] Dynamic script loading is no longer used.`);
    console.log(`Script ${src} is now loaded directly in HTML.`);
    
    // 즉시 콜백 실행 (기존 코드와의 호환성을 위해)
    if (callback) {
        console.log(`Executing callback for ${src}`);
        setTimeout(callback, 0);
    }
}
